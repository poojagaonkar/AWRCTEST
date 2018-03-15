package adweb.com.awteamestimates;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import adweb.com.awteamestimates.Fragments.IssueListDialogFragment;
import adweb.com.awteamestimates.Models.CurrentEstimatedIssue;
import adweb.com.awteamestimates.Models.GetRoles.RoleIdModel;
import adweb.com.awteamestimates.Models.GetRoles.RoleModel;
import adweb.com.awteamestimates.Models.ProjectModel;
import adweb.com.awteamestimates.Models.TeamEstimationsRolesDatum;
import adweb.com.awteamestimates.Service.JiraServices;
import adweb.com.awteamestimates.Utilities.AppConstants;

public class IssueSummaryActivity extends AppCompatActivity implements  IssueListDialogFragment.Listener, View.OnClickListener{

    private ArrayList<CharSequence> optionsList;
    private Spinner mSpinRole;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEdit;
    private String mBaseUrl;
    private String mUserName;
    private TextView txtIssueTitle;
    private TableRow rowOrgEstimate;
    private TableRow rowMyEstimate;
    private TextView txtOrgEstimate;
    private TextView txtMyEstimate;
    private Collection<RoleIdModel> mRoleIdCollection;
    private RoleIdModel mCurrentRole;
    private CardView cardView;
    private Button btnCancelBottomSheet;
    private  TextView toolbarTxtProjectTitle;
    private String myPreviousEstimate ="";


    @Override
    protected  void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSpinRole = (Spinner) findViewById(R.id.mSpinRole);
        txtIssueTitle = (TextView)findViewById(R.id.mTxtIssueTitle);
        rowOrgEstimate = (TableRow)findViewById(R.id.tableRowOrgEstimate);
        rowMyEstimate = (TableRow)findViewById(R.id.tableRowMyEstimate);
        txtOrgEstimate = (TextView)rowOrgEstimate.findViewById(R.id.txtOrgEstimate);
        txtMyEstimate = (TextView)rowMyEstimate.findViewById(R.id.txtMyEstimate);
        cardView = (CardView)findViewById(R.id.card_view);
        toolbarTxtProjectTitle = toolbar.findViewById(R.id.toolbarProjectTitle);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        toolbarTxtProjectTitle.setText(AppConstants.CurrentSelectedProject);


        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit = mPrefs.edit();
        mBaseUrl = mPrefs.getString(getResources().getString(R.string.pref_baseUrl), null);
        mUserName = mPrefs.getString(getResources().getString(R.string.pref_userName), null);

        try {
                    if (AppConstants.isRoleEnabled) {
                        mSpinRole.setVisibility(View.VISIBLE);

                        if(AppConstants.CurrentEstimatedIssue.getOriginalEstimate()!=null && !AppConstants.CurrentEstimatedIssue.getOriginalEstimate().toString().isEmpty())
                        {
                            txtOrgEstimate.setText(AppConstants.CurrentEstimatedIssue.getOriginalEstimate());

                        }
                        else
                        {
                            txtOrgEstimate.setText(getResources().getString(R.string.DefaultEstimateString));

                        }
                        GetRoles();

                        if (mRoleIdCollection == null) {
                            mRoleIdCollection = Collections2.filter(AppConstants.ProjectRoleList, val -> val.getRoleName().equals(AppConstants.CurrentSelectedRole));
                            mCurrentRole = mRoleIdCollection.iterator().next();
                        }


                        AppConstants.CurrentRoleDetails = mCurrentRole;

                        if(mCurrentRole.getRoleEstimate()!=null && !mCurrentRole.getRoleEstimate().toString().isEmpty()) {



                             myPreviousEstimate = AppConstants.MyEstimatedIssuesMap.get(AppConstants.CurrentEstimatedIssue.getIssueKey()) ;
                            if(myPreviousEstimate !=null && !myPreviousEstimate.isEmpty() && !myPreviousEstimate.matches(mCurrentRole.getRoleEstimate().toString()))
                            {
                                txtMyEstimate.setText(myPreviousEstimate);
                            }
                            else {
                                txtMyEstimate.setText(mCurrentRole.getRoleEstimate());
                            }
                        }
                        else
                        {
                            txtMyEstimate.setText(getResources().getString(R.string.DefaultEstimateString));

                        }
                    }

                    txtIssueTitle.setText(AppConstants.CurrentEstimatedIssue.getIssueTitle());


                        //Show Bottom Sheet options
                        View view = getLayoutInflater().inflate(R.layout.fragment_issue_list_dialog, null);

                        optionsList = new ArrayList<>();
                        optionsList.add("Estimate Issue");
                        optionsList.add("More Details");

                        cardView.setOnClickListener(this);
        }
        catch (Exception ex)
        {
              AlertDialog.Builder mAlert = new AlertDialog.Builder(this);
              mAlert.setMessage(ex.getMessage());
              mAlert.setTitle("Error");
              mAlert.setCancelable(false);
              mAlert.setPositiveButton("Ok",null);
              mAlert.create().show();
        }

    }

    private void GetRoles() {
                try {
            JiraServices.GetRoleDetails getRoleDetails = new JiraServices.GetRoleDetails(this, mUserName, mBaseUrl, AppConstants.CurrentEstimatedIssue.getIssueKey());
            RoleModel mModel = getRoleDetails.execute().get();

            if(mModel !=null && mModel.getRoles()!= null && mModel.getRoles().size() >0)
            {

                AppConstants.ProjectRoleTitles =new ArrayList<String>(mModel.getRoles().values());

                AppConstants.ProjectRoleList = new ArrayList<RoleIdModel>();
                Iterator it = mModel.getRoles().entrySet().iterator();
                while (it.hasNext())
                {
                    Map.Entry pair = (Map.Entry)it.next();
                    RoleIdModel pModel = new RoleIdModel();
                    pModel.setRoleID(String.valueOf(pair.getKey()));
                    pModel.setRoleName(String.valueOf(pair.getValue()));
                    if(AppConstants.CurrentEstimatedIssue.getTeamEstimationsRolesData() != null && AppConstants.CurrentEstimatedIssue.getTeamEstimationsRolesData().size() >0)
                    {
                        for(TeamEstimationsRolesDatum bModel : AppConstants.CurrentEstimatedIssue.getTeamEstimationsRolesData())
                        {
                            if(bModel.getRoleid()!= null &&  bModel.getRoleid().equals(pair.getKey()))
                            {
                                pModel.setRoleEstimatee(bModel.getEstimateFormatted());
                            }
                        }

                    }

                    AppConstants.ProjectRoleList.add(pModel);
                }

                final ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, AppConstants.ProjectRoleTitles);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinRole.setAdapter(adapter);
                mSpinRole.setVisibility(View.VISIBLE);

                AppConstants.CurrentSelectedRole = adapter.getItem(0);
                mSpinRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        AppConstants.CurrentSelectedRole = adapter.getItem(i);
                        if(mRoleIdCollection != null) {
                            mRoleIdCollection = Collections2.filter(AppConstants.ProjectRoleList, val -> val.getRoleName().equals(AppConstants.CurrentSelectedRole));
                            mCurrentRole = mRoleIdCollection.iterator().next();
                        }

                        AppConstants.CurrentRoleDetails = mCurrentRole;
                        if(mCurrentRole.getRoleEstimate()!=null && !mCurrentRole.getRoleEstimate().toString().isEmpty()) {

                            if(AppConstants.CurrentEstimatedIssue.getOriginalEstimate()!=null && !AppConstants.CurrentEstimatedIssue.getOriginalEstimate().toString().isEmpty())
                            {
                                txtOrgEstimate.setText(AppConstants.CurrentEstimatedIssue.getOriginalEstimate());

                            }
                            else {

                                txtOrgEstimate.setText(getResources().getString(R.string.DefaultEstimateString));

                            }

                            if(mCurrentRole.getRoleEstimate() != null && !mCurrentRole.getRoleEstimate().isEmpty())
                            {
                                txtMyEstimate.setText(mCurrentRole.getRoleEstimate());
                            }
                            else {
                                String myPreviousEstimate = AppConstants.MyEstimatedIssuesMap.get(AppConstants.CurrentEstimatedIssue.getIssueKey());
                                if (myPreviousEstimate != null && !myPreviousEstimate.isEmpty() && !myPreviousEstimate.matches(mCurrentRole.getRoleEstimate().toString())) {
                                    txtMyEstimate.setText(myPreviousEstimate);
                                }
                            }
                        }
                        else
                        {
                            txtMyEstimate.setText(getResources().getString(R.string.DefaultEstimateString));

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        AppConstants.CurrentSelectedRole = adapter.getItem(0);
                    }
                });

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onIssueClicked(int position) {

        switch (position)
        {
            case 0:
                this.startActivityForResult(new Intent(this, EstimateIssueActivity.class), 0);
                break;
            case 1:
                this.startActivity(new Intent(this, IssueDetailsActivity.class));

                break;
            default:
                    break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

                // get String data from Intent
                String returnString = data.getStringExtra("MyEstimates");

                // set text view with string

              txtMyEstimate.setText(returnString);

                GetProjectsDetails();


            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.estimate_menu, menu);

        MenuItem action_refresh = menu.findItem(R.id.action_refresh);
        Drawable drawable = action_refresh.getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_refresh:

                GetProjectsDetails();
                Toast.makeText(this, "Refreshed.", Toast.LENGTH_SHORT) .show();
                break;
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }

        return true;
    }

    private void GetProjectsDetails() {

        try {
            JiraServices.GetProjectDetails getProjectDetails = new JiraServices.GetProjectDetails(this,  mUserName, mBaseUrl);
            ProjectModel mModel = getProjectDetails.execute().get();

            AppConstants.isRefreshed = false;

            if(mModel!=null)
            {
                AppConstants.FullProjectList = mModel.getCurrentEstimatedIssue();
                Collections.reverse(AppConstants.FullProjectList);
                AppConstants.ProjectTitles = new ArrayList<>();

                for (CurrentEstimatedIssue mIssue : AppConstants.FullProjectList) {

                    AppConstants.ProjectTitles.add(mIssue.getProjectName());
                    System.out.println(mIssue.getIssueKey() + "\n" + mIssue.getIssueTitle() + "\n" + mIssue.getProjectKey() + "\n" + mIssue.getProjectName());

                }

                AppConstants.CurrentIssueDetails = Collections2.filter(AppConstants.FullProjectList, user -> user.getProjectName().equals(AppConstants.CurrentSelectedProject)).iterator();
                AppConstants.CurrentEstimatedIssue  = AppConstants.CurrentIssueDetails.next();

                txtIssueTitle.setText(AppConstants.CurrentEstimatedIssue.getIssueTitle());
                 this.recreate();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.card_view:
                IssueListDialogFragment.newInstance(optionsList).show(getSupportFragmentManager(), "dialog");

                break;

        }
    }
}

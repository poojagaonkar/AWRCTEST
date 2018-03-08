package adweb.com.awteamestimates;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import adweb.com.awteamestimates.Fragments.IssueListDialogFragment;
import adweb.com.awteamestimates.Models.GetRoles.RoleIdModel;
import adweb.com.awteamestimates.Models.GetRoles.RoleModel;
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
        txtMyEstimate = (TextView)rowOrgEstimate.findViewById(R.id.txtMyEstimate);
        cardView = (CardView)findViewById(R.id.card_view);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);



        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit = mPrefs.edit();
        mBaseUrl = mPrefs.getString(getResources().getString(R.string.pref_baseUrl), null);
        mUserName = mPrefs.getString(getResources().getString(R.string.pref_userName), null);


        if(AppConstants.isRoleEnabled) {
            mSpinRole.setVisibility(View.VISIBLE);
            GetRoles();
        }

        txtIssueTitle.setText(AppConstants.CurrentEstimatedIssue.getIssueTitle());

        if(mRoleIdCollection == null) {
            mRoleIdCollection = Collections2.filter(AppConstants.ProjectRoleList, val -> val.getRoleName().equals(AppConstants.CurrentSelectedRole));
            mCurrentRole = mRoleIdCollection.iterator().next();
        }

        txtOrgEstimate.setText(mCurrentRole.getRoleEstimate());


        //Show Bottom Sheet options
        View view = getLayoutInflater().inflate(R.layout.fragment_issue_list_dialog, null);

        optionsList = new ArrayList<>();
        optionsList.add("Estimate Issue");
        optionsList.add("More Details");

        cardView.setOnClickListener(this);

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
                        if(mRoleIdCollection == null) {
                            mRoleIdCollection = Collections2.filter(AppConstants.ProjectRoleList, val -> val.getRoleName().equals(AppConstants.CurrentSelectedRole));
                            mCurrentRole = mRoleIdCollection.iterator().next();
                        }

                        txtOrgEstimate.setText(mCurrentRole.getRoleEstimate());
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

    }

    @Override
    public void onIssueClicked(int position) {

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
                Toast.makeText(this, "Refreshing..", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.homeAsUp:
                this.finish();
                break;
            default:
                break;
        }

        return true;
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

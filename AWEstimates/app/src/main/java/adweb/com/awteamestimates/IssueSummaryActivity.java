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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
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

public class IssueSummaryActivity extends AppCompatActivity implements  IssueListDialogFragment.Listener{

    private ArrayList<CharSequence> optionsList;
    private Spinner mSpinRole;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEdit;
    private String mBaseUrl;
    private String mUserName;


    @Override
    protected  void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        mSpinRole = (Spinner) findViewById(R.id.mSpinRole);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit = mPrefs.edit();
        mBaseUrl = mPrefs.getString(getResources().getString(R.string.pref_baseUrl), null);
        mUserName = mPrefs.getString(getResources().getString(R.string.pref_userName), null);


        if(AppConstants.isRoleEnabled) {
            mSpinRole.setVisibility(View.VISIBLE);
            GetRoles();
        }
        //Show Bottom Sheet options
        View view = getLayoutInflater().inflate(R.layout.fragment_issue_list_dialog, null);

        optionsList = new ArrayList<>();
        optionsList.add("Estimate Issue");
        optionsList.add("More Details");


        IssueListDialogFragment.newInstance(optionsList).show(this.getSupportFragmentManager(), "dialog");
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

                mSpinRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        AppConstants.CurrentSelectedRole = adapter.getItem(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

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

            default:
                break;
        }

        return true;
    }
}

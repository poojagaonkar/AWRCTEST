package adweb.com.awteamestimates;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import adweb.com.awteamestimates.Adapters.MoreDetailsAdapter;
import adweb.com.awteamestimates.Models.IssuesMoreDetails.MoreDetailModel;
import adweb.com.awteamestimates.Service.JiraServices;
import adweb.com.awteamestimates.Utilities.AppConstants;

public class IssueDetailsActivity extends AppCompatActivity {

    private ListView lvMoreList;
    public SharedPreferences mPrefs ;
    public  SharedPreferences.Editor mEdit ;
    private  String mUserName ;
    private  String mBaseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue_details_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit = mPrefs.edit();

        mBaseUrl = mPrefs.getString(getResources().getString(R.string.pref_baseUrl), null);
        mUserName = mPrefs.getString(getResources().getString(R.string.pref_userName), null);

        try {
            JiraServices.GetMoreDetails mauth= new JiraServices.GetMoreDetails(this,mUserName, mBaseUrl, AppConstants.CurrentEstimatedIssue.getIssueKey());

            MoreDetailModel mModel = mauth.execute().get();

            if(mModel !=null)
            {
                AppConstants.CurrentProjectDetailMap= new HashMap<String, String>();
                AppConstants.CurrentProjectDetailMap.put("Project Name :  ", AppConstants.CurrentEstimatedIssue.getProjectName());
                AppConstants.CurrentProjectDetailMap.put("Description :  ", mModel.getFields().getIssuetype().getDescription());
                AppConstants.CurrentProjectDetailMap.put("Issue Type :  ",  mModel.getFields().getIssuetype().getName());
                AppConstants.CurrentProjectDetailMap.put("Creator :  ",mModel.getFields().getCreator().getDisplayName());
                AppConstants.CurrentProjectDetailMap.put("Reporter :  ", mModel.getFields().getReporter().getDisplayName());
                AppConstants.CurrentProjectDetailMap.put("Priority :  ", mModel.getFields().getPriority().getName());

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        lvMoreList = (ListView)findViewById(R.id.lvIssueDetails);


        lvMoreList.setAdapter(new MoreDetailsAdapter(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);


    }
}

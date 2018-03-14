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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ahmadrosid.svgloader.SvgLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import adweb.com.awteamestimates.Adapters.MoreDetailsAdapter;
import adweb.com.awteamestimates.Models.IssuesMoreDetails.MoreDetailModel;
import adweb.com.awteamestimates.Models.MoreIssueDetailModel;
import adweb.com.awteamestimates.Service.JiraServices;
import adweb.com.awteamestimates.Utilities.AppConstants;

public class IssueDetailsActivity extends AppCompatActivity {

    private ListView lvMoreList;
    public SharedPreferences mPrefs ;
    public  SharedPreferences.Editor mEdit ;
    private  String mUserName ;
    private  String mBaseUrl;
    private TextView txtToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue_details_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         txtToolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarIssueDetailsTitle);
        ImageView imgToolbarImg = (ImageView) toolbar.findViewById(R.id.toolBarissueIcon);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));

        txtToolbarTitle.setText(AppConstants.CurrentEstimatedIssue.getIssueTitle());

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit = mPrefs.edit();

        mBaseUrl = mPrefs.getString(getResources().getString(R.string.pref_baseUrl), null);
        mUserName = mPrefs.getString(getResources().getString(R.string.pref_userName), null);

        String iconUrl = AppConstants.BaseUrl  + AppConstants.CurrentEstimatedIssue.getAvatarIssue();

        SvgLoader.pluck()
                .with(this)
                .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                .load(iconUrl, imgToolbarImg);



        try {
            JiraServices.GetMoreDetails mauth= new JiraServices.GetMoreDetails(this,mUserName, mBaseUrl, AppConstants.CurrentEstimatedIssue.getIssueKey());

            MoreDetailModel mModel = mauth.execute().get();

            if(mModel !=null)
            {
                AppConstants.CurrentProjectDetailList= new ArrayList<MoreIssueDetailModel>();
                AppConstants.CurrentProjectDetailList.add(new MoreIssueDetailModel("Issue Title : ", AppConstants.CurrentEstimatedIssue.getIssueTitle()));
                AppConstants.CurrentProjectDetailList.add(new MoreIssueDetailModel("Project Name :  ", AppConstants.CurrentEstimatedIssue.getProjectName()));
                AppConstants.CurrentProjectDetailList.add(new MoreIssueDetailModel("Description :  ", mModel.getFields().getIssuetype().getDescription()));
                AppConstants.CurrentProjectDetailList.add(new MoreIssueDetailModel("Issue Type :  ",  mModel.getFields().getIssuetype().getName()));
                AppConstants.CurrentProjectDetailList.add(new MoreIssueDetailModel("Creator :  ",mModel.getFields().getCreator().getDisplayName()));
                AppConstants.CurrentProjectDetailList.add(new MoreIssueDetailModel("Reporter :  ", mModel.getFields().getReporter().getDisplayName()));
                AppConstants.CurrentProjectDetailList.add(new MoreIssueDetailModel("Priority :  ", mModel.getFields().getPriority().getName()));

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

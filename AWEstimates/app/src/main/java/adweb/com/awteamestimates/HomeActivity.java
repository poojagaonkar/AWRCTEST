package adweb.com.awteamestimates;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import adweb.com.awteamestimates.Models.CurrentEstimatedIssue;
import adweb.com.awteamestimates.Models.LoginModel;
import adweb.com.awteamestimates.Models.ProjectModel;
import adweb.com.awteamestimates.Models.UserModel;
import adweb.com.awteamestimates.Service.ApiUrls;
import adweb.com.awteamestimates.Service.JiraServices;
import adweb.com.awteamestimates.Utilities.AppConstants;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public SharedPreferences mPrefs ;
    public  SharedPreferences.Editor mEdit ;
    private  String mUserName ;
    private  String mSessionUserName;
    private  String mSessionUserValue;
    private  String mBaseUrl;
    private TextView txtUserName;
    private TextView txtUserEmail;
    private  TextView txtTemp;
    private Spinner mProjectSpinner;
    private ImageButton btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtTemp = (TextView)findViewById(R.id.txtSelect);

        mProjectSpinner = findViewById(R.id.spinProjectName);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        txtUserName= (TextView)navigationView.getHeaderView(0).findViewById(R.id.txtUserName);
        txtUserEmail= (TextView)navigationView.getHeaderView(0).findViewById(R.id.txtUserEmail);
        btnNext = findViewById(R.id.btnNext);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit = mPrefs.edit();


        mBaseUrl = mPrefs.getString(getResources().getString(R.string.pref_baseUrl), null);
        mUserName = mPrefs.getString(getResources().getString(R.string.pref_userName), null);



        //region Get user details
        try {
            JiraServices. GetUserDetails getUserDetails = new JiraServices.GetUserDetails(mUserName,mBaseUrl);
            UserModel mModel = getUserDetails.execute().get();

            if(mModel != null ) {
                String userFirstName = mModel.getDisplayName();
                String userEmail = mModel.getEmailAddress();
                //   String avrUrl = mModel.getAvatarUrls().get48x48();
                System.out.println(userFirstName + "," + userEmail + ",");

                mEdit.putString(getResources().getString(R.string.pref_userDisplayName), userFirstName);
                mEdit.putString(getResources().getString(R.string.pref_userEmail), userEmail);
                mEdit.commit();

                txtUserEmail.setText(userEmail);
                txtUserName.setText(userFirstName);
            }
            else
            {
                throw new RuntimeException("Could not fetch user details.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        //endregion



        //region Get Project Details
        try {
            JiraServices. GetProjectDetails getProjectDetails = new JiraServices.GetProjectDetails(mUserName,mBaseUrl);
            ProjectModel mModel = getProjectDetails.execute().get();

            if(mModel != null ) {
                AppConstants.FullProjectList = mModel.getCurrentEstimatedIssue();
                List<String> projectTitles  = new ArrayList<>();

                for(CurrentEstimatedIssue mIssue : AppConstants.FullProjectList )
               {

                   projectTitles.add(mIssue.getProjectName());
                   System.out.println(mIssue.getIssueKey() +"\n" + mIssue.getIssueTitle() +"\n"+ mIssue.getProjectKey() +"\n"+ mIssue.getProjectName());

               }

                final ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, projectTitles);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mProjectSpinner.setAdapter(adapter);



                mProjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        AppConstants.CurrentSelectedProject =adapter.getItem(i);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        AppConstants.CurrentSelectedProject =adapter.getItem(0);

                    }
                });

                btnNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent mIntent = new Intent();
                        mIntent.setClass(getApplicationContext(), ProjectEstimateActivity.class);
                        startActivity(mIntent);
                    }
                });
            }
            else
            {
                throw new RuntimeException("Could not fetch project details.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        //endregion



    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_project) {
            // Handle the camera action
        } else if (id == R.id.nav_logout) {


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}

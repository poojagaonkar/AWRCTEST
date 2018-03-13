package adweb.com.awteamestimates;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
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

import adweb.com.awteamestimates.Fragments.ProjectEstimatationFragment;
import adweb.com.awteamestimates.Fragments.SelectProjectFragment;
import adweb.com.awteamestimates.Models.CurrentEstimatedIssue;
import adweb.com.awteamestimates.Models.LoginModel;
import adweb.com.awteamestimates.Models.ProjectModel;
import adweb.com.awteamestimates.Models.UserModel;
import adweb.com.awteamestimates.Service.ApiUrls;
import adweb.com.awteamestimates.Service.JiraServices;
import adweb.com.awteamestimates.Utilities.AppConstants;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SelectProjectFragment.OnFragmentInteractionListener, DrawerLayout.DrawerListener {

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
    private TextView txtToolBarTitle;
    private Button btnProjectNext;
    public Toolbar toolbar;
    public NavigationView navigationView;
    private ImageView imgUserImage;
    private TextView counterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer.setDrawerListener(this);

        txtUserName= (TextView)navigationView.getHeaderView(0).findViewById(R.id.txtUserName);
        txtUserEmail= (TextView)navigationView.getHeaderView(0).findViewById(R.id.txtUserEmail);
        txtToolBarTitle = (TextView)toolbar.findViewById(R.id.txtToolbarTitle);
        counterText = (TextView) navigationView.getMenu().findItem(R.id.nav_project).getActionView().findViewById(R.id.navCountBubble);
//        btnProjectNext = findViewById(R.id.btnProjectNext);
        imgUserImage =(ImageView) navigationView.getHeaderView(0).findViewById(R.id.imgImageUser);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit = mPrefs.edit();



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);



        mBaseUrl = mPrefs.getString(getResources().getString(R.string.pref_baseUrl), null);
        mUserName = mPrefs.getString(getResources().getString(R.string.pref_userName), null);
        AppConstants.isLoggedOut = mPrefs.getBoolean(getResources().getString(R.string.pref_loggedOut),false);



        loadFragment(new SelectProjectFragment());

        GetUserDetails();

    }

    public  void updateCounter(int count)
    {
        if(count > 0 ) {
            counterText.setVisibility(View.VISIBLE);
            counterText.setText(String.valueOf(count));
        }
        else
        {
            counterText.setVisibility(View.INVISIBLE);
        }
    }

    private void GetUserDetails()
    {
        //region Get user details
        try {
            JiraServices. GetUserDetails getUserDetails = new JiraServices.GetUserDetails(this, mUserName,mBaseUrl);
            UserModel mModel = getUserDetails.execute().get();

            if(mModel != null ) {
                String userFirstName = mModel.getDisplayName();
                String userEmail = mModel.getEmailAddress();
                String userImageUrl = mModel.getAvatarUrls().get48x48();
                //   String avrUrl = mModel.getAvatarUrls().get48x48();
                System.out.println(userFirstName + "," + userEmail + ",");

                mEdit.putString(getResources().getString(R.string.pref_userDisplayName), userFirstName);
                mEdit.putString(getResources().getString(R.string.pref_userEmail), userEmail);
                mEdit.commit();


                Picasso.with(this)
                        .load(userImageUrl)
                        .placeholder(R.drawable.ic_person_outline_black_36dp)
                        .error(R.drawable.ic_person_outline_black_36dp)
                        .into(imgUserImage);

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
    }

    private  void GetProjectDetails()
    {
        //region Get Project Details
        try {
            JiraServices. GetProjectDetails getProjectDetails = new JiraServices.GetProjectDetails(this,mUserName,mBaseUrl);
            ProjectModel mModel = getProjectDetails.execute().get();

            if(mModel != null ) {
                AppConstants.FullProjectList = mModel.getCurrentEstimatedIssue();
                AppConstants.ProjectTitles  = new ArrayList<>();

                for(CurrentEstimatedIssue mIssue : AppConstants.FullProjectList )
                {

                    AppConstants.ProjectTitles.add(mIssue.getProjectName());
                    System.out.println(mIssue.getIssueKey() +"\n" + mIssue.getIssueTitle() +"\n"+ mIssue.getProjectKey() +"\n"+ mIssue.getProjectName());

                }

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

            AppConstants.isRefreshed = true;
            GetUserDetails();

            Fragment f =this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);


            final android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(f);
            ft.attach(f);
            ft.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment mFragment = null;
        if (id == R.id.nav_project) {

            mFragment = new SelectProjectFragment();
            txtToolBarTitle.setText("ESTIMATION");
            AppConstants.isRefreshed = true;
            // Handle the camera action
        } else if (id == R.id.nav_logout) {

            AlertDialog.Builder mAlert = new AlertDialog.Builder(this, R.style.CustomDialog);
            mAlert.setTitle("Logout");
            mAlert.setMessage("Are you sure?");
            mAlert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    mEdit.putString(getResources().getString(R.string.pref_sessionUserName), "");
                    mEdit.putString(getResources().getString(R.string.pref_sessionUserValue), "");
                    mEdit.putBoolean(getResources().getString(R.string.pref_loggedOut), true);
                    mEdit.commit();

                    AppConstants.estimatedIssueKeys = null;
                    AppConstants.FullProjectList = null;
                    AppConstants.isEstimated = false;
                    AppConstants.ClearAll();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            });
            mAlert.setNegativeButton("No", null);
            mAlert.create().show();




        }

        if(mFragment != null) {


            loadFragment(mFragment);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void loadFragment(Fragment fragment) {
        // create a FragmentManager
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
                android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commit(); // save the changes
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

        counterText.setText(String.valueOf(Integer.valueOf(counterText.getText().toString()) - AppConstants.estimatedIssueKeys.size()));
    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}

package adweb.com.awteamestimates;

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
import android.widget.TextView;

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

import javax.net.ssl.HttpsURLConnection;

import adweb.com.awteamestimates.Models.LoginModel;
import adweb.com.awteamestimates.Models.UserModel;
import adweb.com.awteamestimates.Service.ApiUrls;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        txtUserName= (TextView)navigationView.getHeaderView(0).findViewById(R.id.txtUserName);
        txtUserEmail= (TextView)navigationView.getHeaderView(0).findViewById(R.id.txtUserEmail);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit = mPrefs.edit();


        mBaseUrl = mPrefs.getString(getResources().getString(R.string.pref_baseUrl), null);
        mUserName = mPrefs.getString(getResources().getString(R.string.pref_userName), null);


        GetUserDetails getUserDetails = new GetUserDetails();
        //Call execute
        getUserDetails.execute();
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


    private class GetUserDetails extends AsyncTask <String, Void, UserModel> {

        @Override
        protected void onPostExecute(UserModel mModel) {
            super.onPostExecute(mModel);

            String userFirstName = mModel.getDisplayName();
            String userEmail = mModel.getEmailAddress();
        //   String avrUrl = mModel.getAvatarUrls().get48x48();
            System.out.println(userFirstName +"," + userEmail+",");

            mEdit.putString(getResources().getString(R.string.pref_userDisplayName), userFirstName);
            mEdit.putString(getResources().getString(R.string.pref_userEmail), userEmail);
            mEdit.commit();

           txtUserEmail.setText(userEmail);
           txtUserName.setText(userFirstName);

        }

        @Override
        protected UserModel doInBackground(String... strings) {
            String auth = new String(Base64.encode(mUserName+":"+ AppConstants.tempPass));
            try {
                String projects = invokeGetMethod(auth,mBaseUrl + ApiUrls.USER_DETAIL_URL + mUserName);
                System.out.println(projects);



                final ObjectMapper mapper = new ObjectMapper();//.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, false);;
                UserModel mModel = mapper.readValue(projects, UserModel.class);
                return  mModel;

                } catch (Exception e) {
                    System.out.println("Username or Password wrong!");
                    e.printStackTrace();
                }
            return null;
        }
    }

    private static String invokeGetMethod(String auth, String url) throws Exception, ClientHandlerException {
        Client client = Client.create();
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
                .accept("application/json").get(ClientResponse.class);
        int statusCode = response.getStatus();
        if (statusCode == 401) {
            throw new Exception("Invalid Username or Password");
        }
        return response.getEntity(String.class);
    }
}

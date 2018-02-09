package adweb.com.awteamestimates.Service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import adweb.com.awteamestimates.HomeActivity;
import adweb.com.awteamestimates.Models.EstimateModel;
import adweb.com.awteamestimates.Models.IssuesMoreDetails.MoreDetailModel;
import adweb.com.awteamestimates.Models.LoginModel;
import adweb.com.awteamestimates.Models.ProjectModel;
import adweb.com.awteamestimates.Models.UserModel;
import adweb.com.awteamestimates.R;
import adweb.com.awteamestimates.Utilities.AppConstants;

/**
 * Created by PoojaGaonkar on 2/6/2018.
 */

public class JiraServices {



    //<editor-fold desc="POST ==> Login">
    public static class UserLoginTask extends AsyncTask<Void, Void, LoginModel> {

        private final String mUserName;
        private final String mPassword;
        private  final  String mBaseUrl;
        private final Activity mContext;
        private ProgressDialog pd;
        private SharedPreferences mPrefs;
        private SharedPreferences.Editor editor;

        public UserLoginTask(Activity loginActivity, String userName, String password, String baseUrl) {
            mUserName = userName;
            mPassword = password;
            mBaseUrl = baseUrl;
            mContext = loginActivity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            editor = mPrefs.edit();

            //DialogHelper.ShowProgressDialog(mContext, false, "");

        }

        @Override
        protected LoginModel doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {

                String credentials = "{\"username\":\""+mUserName+"\",\"password\":\""+mPassword+"\"}";

                // String credentials = "{\"username\":\"admin\",\"password\":\"admin\"}";

                URL url = new URL(mBaseUrl + ApiUrls.LOGIN_URL );
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");


                OutputStream os = conn.getOutputStream();
                os.write(credentials.getBytes());
                os.close();

                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
                {
                    throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String output;

                while ((output = br.readLine()) != null) {

                    System.out.println("Output from Server .... \n" + output);
                    final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, false);;
                    try {

                        LoginModel mModel = mapper.readValue(output, LoginModel.class);

                        return  mModel;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }



                conn.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            // TODO: register the new account here.
            return null;
        }

        @Override
        protected void onPostExecute(final LoginModel mModel) {


            if (mModel != null) {

                String mUserSessionName = mModel.getSession().getName() ;
                String mUserSessionValue = mModel.getSession().getValue();

                //On successful login, save the variables for next use.
                editor.putString(mContext.getResources().getString(R.string.pref_sessionUserName), mUserSessionName);
                editor.putString(mContext.getResources().getString(R.string.pref_sessionUserValue), mUserSessionValue);
                editor.putString(mContext.getResources().getString(R.string.pref_baseUrl), mBaseUrl);
                editor.putString(mContext.getResources().getString(R.string.pref_userName), mUserName);
                AppConstants.tempPass = mPassword;
                editor.commit();

                //Start next activity
                Intent mIntent = new Intent(mContext, HomeActivity.class);
                mContext.startActivity(mIntent);
                mContext.finish();
            } else {
//                mPasswordView.setError(getString(R.string.login_failed));
//                mPasswordView.requestFocus();
            }

            //DialogHelper.ShowProgressDialog(mContext, true, "");

        }

        @Override
        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
        }
    }
    //</editor-fold>

    //<editor-fold desc="GET ==> Get User Details">
    public static class GetUserDetails extends AsyncTask <String, Void, UserModel> {

        private final String mUserName;
        private final String mBaseUrl;

        public  GetUserDetails(String userName, String baseUrl)
        {
            mUserName = userName;
            mBaseUrl = baseUrl;
        }

        @Override
        protected void onPostExecute(UserModel mModel) {
            super.onPostExecute(mModel);



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

        private  String invokeGetMethod(String auth, String url) throws Exception, ClientHandlerException {
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
    //</editor-fold>

    //<editor-fold desc="GET ==> Get Project Details">
    public static class GetProjectDetails extends AsyncTask <String, Void, ProjectModel> {

        private final String mUserName;
        private final String mBaseUrl;

        public  GetProjectDetails(String userName, String baseUrl)
        {
            mUserName = userName;
            mBaseUrl = baseUrl;
        }

        @Override
        protected void onPostExecute(ProjectModel mModel) {
            super.onPostExecute(mModel);



        }

        @Override
        protected ProjectModel doInBackground(String... strings) {
            String auth = new String(Base64.encode(mUserName+":"+ AppConstants.tempPass));
            try {
                String projects = invokeGetMethod(auth,mBaseUrl + ApiUrls.FETCH_PROJECTS_URL);
                System.out.println(projects);



                final ObjectMapper mapper = new ObjectMapper();//.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, false);;
                ProjectModel mModel = mapper.readValue(projects, ProjectModel.class);
                return  mModel;

            } catch (Exception e) {
                System.out.println("Username or Password wrong!");
                e.printStackTrace();
            }
            return null;
        }

        private  String invokeGetMethod(String auth, String url) throws Exception, ClientHandlerException {
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
    //</editor-fold>


    //<editor-fold desc="POST ==> Submit Estimate">
    public static class SubmitEstimateTask extends AsyncTask<Void, Void, EstimateModel>  {

        private final String mUserName;
        private  final  String mBaseUrl;
        private  final String mEstimateString;
        private  final String mIssueKey;


        public SubmitEstimateTask(String userName, String baseUrl, String estimateString, String issueKey) {
            mUserName = userName;
            mBaseUrl = baseUrl;
            mEstimateString = estimateString;
            mIssueKey = issueKey;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected EstimateModel doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {


                String auth = new String(Base64.encode(mUserName+":"+ AppConstants.tempPass));

                try {


                    String projects = invokePostMethod(auth,mBaseUrl + ApiUrls.FETCH_PROJECTS_URL);
                    System.out.println(projects);



                    final ObjectMapper mapper = new ObjectMapper();
                    EstimateModel mModel = mapper.readValue(projects, EstimateModel.class);
                    return  mModel;

                } catch (Exception e) {
                    System.out.println("Username or Password wrong!");
                    e.printStackTrace();
                }



            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            // TODO: register the new account here.
            return null;
        }

        private String invokePostMethod(String auth, String url)  throws Exception, ClientHandlerException {

            String input = "{\"userName\":\""+mUserName+"\",\"issueKey\":\""+mIssueKey+"\",\"teamEstimate\":\""+mEstimateString+"\"}";


            Client client = Client.create();
                WebResource webResource = client.resource(mBaseUrl + ApiUrls.SUBMIT_ESTIMATION_URL );
            ClientResponse response = webResource.type("application/json")
                    .header("Authorization", "Basic " + auth)
                    .post(ClientResponse.class, input);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            System.out.println("Output from Server .... \n");
            String output = response.getEntity(String.class);
            System.out.println(output);
            return  output;
        }

        @Override
        protected void onPostExecute(final EstimateModel mModel) {

        }

        @Override
        protected void onCancelled() {
    }
    }
    //</editor-fold>


    //<editor-fold desc="GET ==> Get More Details">
    public static class GetMoreDetails extends AsyncTask <String, Void, MoreDetailModel> {

        private final String mUserName;
        private final String mBaseUrl;
        private final String mIssueKey;

        public  GetMoreDetails(String userName, String baseUrl, String issueKey)
        {
            mUserName = userName;
            mBaseUrl = baseUrl;
            mIssueKey = issueKey;
        }

        @Override
        protected void onPostExecute(MoreDetailModel mModel) {
            super.onPostExecute(mModel);



        }

        @Override
        protected MoreDetailModel doInBackground(String... strings) {
            String auth = new String(Base64.encode(mUserName+":"+ AppConstants.tempPass));
            try {
                String projects = invokeGetMethod(auth,mBaseUrl + ApiUrls.MORE_DETAILS_URL + mIssueKey);
                System.out.println(projects);



                final ObjectMapper mapper = new ObjectMapper();//.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, false);;
                MoreDetailModel mModel = mapper.readValue(projects, MoreDetailModel.class);
                return  mModel;

            } catch (Exception e) {
                System.out.println("Username or Password wrong!");
                e.printStackTrace();
            }
            return null;
        }

        private  String invokeGetMethod(String auth, String url) throws Exception, ClientHandlerException {
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
    //</editor-fold>
}

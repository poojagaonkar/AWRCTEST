package adweb.com.awteamestimates;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import adweb.com.awteamestimates.Models.LoginModel;
import adweb.com.awteamestimates.Models.UserModel;
import adweb.com.awteamestimates.Service.ApiUrls;
import adweb.com.awteamestimates.Service.JiraServices;
import adweb.com.awteamestimates.Utilities.AppConstants;
import adweb.com.awteamestimates.Utilities.DialogHelper;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via userName/password.
 */
public class LoginActivity extends AppCompatActivity  {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    public JiraServices.UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mUserNameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
 private  EditText mBaseUrlView;
    private String userName;
    private  String baseUrl;


    private   String mUserSessionName ="";
    private   String mUserSessionValue ="";
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor editor;
    public Button mUserNameSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // Set up the login form.
        mUserNameView = (EditText) findViewById(R.id.email);
        mBaseUrlView = (EditText)findViewById(R.id.etBaseUrl);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {

                    HideKeyboard(mPasswordView);
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


         mUserNameSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mUserNameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard(view);

                attemptLogin();
            }
        });

         mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
         editor = mPrefs.edit();

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        baseUrl = mPrefs.getString(getResources().getString(R.string.pref_baseUrl), null);
        userName = mPrefs.getString(getResources().getString(R.string.pref_userName), null);

        if(!TextUtils.isEmpty(baseUrl) && !TextUtils.isEmpty(userName))
        {
            mBaseUrlView.setText(baseUrl);
            mUserNameView.setText(userName);
        }




    }

    private void HideKeyboard(View v)
    {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }






    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        try {
//            if (mAuthTask != null) {
//                return;
//            }

            // Reset errors.
            mUserNameView.setError(null);
            mPasswordView.setError(null);
            mBaseUrlView.setError(null);
            mUserNameSignInButton.setEnabled(true);

            // Store values at the time of the login attempt.
            String password = mPasswordView.getText().toString();
            baseUrl = mBaseUrlView.getText().toString();
            userName = mUserNameView.getText().toString();


            boolean cancel = false;
            View focusView = null;

            // Check for a valid baseurl, if the user entered one.

            if (TextUtils.isEmpty(baseUrl)) {
                mBaseUrlView.setError(getString(R.string.error_invalid_baseurl));
                focusView = mBaseUrlView;
                cancel = true;
            }

            // Check for a valid password, if the user entered one.
            if (TextUtils.isEmpty(password)) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }

            // Check for a valid userName address.
            if (TextUtils.isEmpty(userName)) {
                mUserNameView.setError(getString(R.string.error_field_required));
                focusView = mUserNameView;
                cancel = true;
            }


            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.

                try {


                    mAuthTask = null;
                    mAuthTask = new JiraServices.UserLoginTask(this, userName, password, baseUrl);

                    mAuthTask.execute();


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
        catch (Exception ex)
        {

        }
    }

    private boolean isEmailValid(String userName) {
        //TODO: Replace this with your own logic
        return userName.contains("@");
    }

//    private boolean isPasswordValid(String password) {
//        //TODO: Replace this with your own logic
//        return password.length() > 4;
//    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



}


package adweb.com.awteamestimates;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import adweb.com.awteamestimates.Models.EstimateModel;
import adweb.com.awteamestimates.Service.JiraServices;
import adweb.com.awteamestimates.Utilities.AppConstants;
import adweb.com.awteamestimates.Utilities.DialogHelper;

public class EstimateIssueActivity extends AppCompatActivity implements View.OnClickListener {

    private TableRow tableRowWeek, tableRowDays, tableRowHours, tableRowMin;
    private EstimateModel estimateModel;
    private JiraServices.SubmitEstimateTask postProjectEstimate;
    private String mEstimateString = "N/A";
    private EditText etWeeks, etDays, etHours, etMins;
    private ImageButton btnAddWeeks, btnAddDays, btnAddHours, btnAddMins;
    private ImageButton btnRemoveWeeks, btnRemoveDays, btnRemoveHours, btnRemoveMins;
    private int weekCounter = 0;
    private int dayCounter = 0;
    private int hourCounter = 0;
    private int minsCounter = 0;
    public SharedPreferences mPrefs ;
    public  SharedPreferences.Editor mEdit ;
    private  String mUserName ;
    private  String mBaseUrl;
    private TextView toolbarTxtIssueTitle;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate_issue);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarTxtIssueTitle = toolbar.findViewById(R.id.toolbarIssueTitle);


        btnAddWeeks = findViewById(R.id.btnCounterUpW);
        btnRemoveWeeks = findViewById(R.id.btnCounterDownW);
        btnAddDays = findViewById(R.id.btnCounterUpD);
        btnRemoveDays = findViewById(R.id.btnCounterDownD);
        btnAddHours = findViewById(R.id.btnCounterUpH);
        btnRemoveHours = findViewById(R.id.btnCounterDownH);
        btnAddMins = findViewById(R.id.btnCounterUpM);
        btnRemoveMins = findViewById(R.id.btnCounterDownM);

        tableRowWeek = findViewById(R.id.tableRowWeek);
        etWeeks = (EditText) tableRowWeek.getChildAt(0);
        tableRowDays = findViewById(R.id.tableRowDays);
        etDays = (EditText) tableRowDays.getChildAt(0);
        tableRowHours = findViewById(R.id.tableRowHours);
        etHours = (EditText) tableRowHours.getChildAt(0);
        tableRowMin = findViewById(R.id.tableRowMins);
        etMins = (EditText) tableRowMin.getChildAt(0);

        btnSubmit = (Button)findViewById(R.id.btnSubmit);


        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        toolbarTxtIssueTitle.setText(AppConstants.CurrentEstimatedIssue.getIssueTitle());



        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit = mPrefs.edit();


        mBaseUrl = mPrefs.getString(getResources().getString(R.string.pref_baseUrl), null);
        mUserName = mPrefs.getString(getResources().getString(R.string.pref_userName), null);


        btnAddWeeks.setOnClickListener(this);
        btnAddHours.setOnClickListener(this);
        btnAddDays.setOnClickListener(this);
        btnAddMins.setOnClickListener(this);

        btnRemoveWeeks.setOnClickListener(this);
        btnRemoveHours.setOnClickListener(this);
        btnRemoveDays.setOnClickListener(this);
        btnRemoveMins.setOnClickListener(this);

        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {

                case R.id.btnCounterUpW:
                    weekCounter++;
                    etWeeks.setText(Integer.toString(weekCounter));
                    break;
                case R.id.btnCounterUpD:
                    dayCounter++;
                    etDays.setText(Integer.toString(dayCounter));

                    break;
                case R.id.btnCounterUpH:
                    hourCounter++;
                    etHours.setText(Integer.toString(hourCounter));

                    break;
                case R.id.btnCounterUpM:
                    minsCounter++;
                    etMins.setText(Integer.toString(minsCounter));

                    break;
                case R.id.btnCounterDownW:

                    if (weekCounter > 0) {
                        weekCounter--;
                        etWeeks.setText(Integer.toString(weekCounter));

                    }
                    break;
                case R.id.btnCounterDownD:
                    if (dayCounter > 0) {
                        dayCounter--;
                        etDays.setText(Integer.toString(dayCounter));

                    }
                    break;
                case R.id.btnCounterDownH:
                    if (hourCounter > 0) {
                        hourCounter--;
                        etHours.setText(Integer.toString(hourCounter));

                    }
                    break;
                case R.id.btnCounterDownM:
                    if (minsCounter > 0) {
                        minsCounter--;
                        etMins.setText(Integer.toString(minsCounter));

                    }
                    break;
                case R.id.btnSubmit:

                    mEstimateString = GetEstimatesString(weekCounter, dayCounter, hourCounter, minsCounter);

                    if (!mEstimateString.isEmpty()) {
                        try {

                            if(!AppConstants.isRoleEnabled) {

                                postProjectEstimate = new JiraServices.SubmitEstimateTask(this, mUserName, mBaseUrl, mEstimateString, AppConstants.CurrentEstimatedIssue.getIssueKey());
                            }
                            else
                            {
                                postProjectEstimate = new JiraServices.SubmitEstimateTask(this, mUserName, mBaseUrl, mEstimateString, AppConstants.CurrentEstimatedIssue.getIssueKey() , AppConstants.CurrentRoleDetails.getRoleID(), AppConstants.CurrentRoleDetails.getRoleName());

                            }


                            estimateModel = postProjectEstimate.execute().get();

                            if (estimateModel != null && estimateModel.getSuccess() == 200) {


                                AppConstants.MyEstimatedIssuesMap.put(estimateModel.getIssueKey(), mEstimateString);
                                Intent intent = new Intent();
                                intent.putExtra("MyEstimates", mEstimateString);
                                setResult(RESULT_OK, intent);
                              //  finish();
                                InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

//                                DialogHelper.ShowAlert(this, "Success", "Estimate submitted successfully", true);
                                etWeeks.setText("");
                                etDays.setText("");
                                etHours.setText("");
                                etMins.setText("");



                            }
                            else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            DialogHelper.ShowAlert(this, "Error", e.getMessage(), false);
                        }

                    } else {

                        Toast.makeText(this, "Please add your estimations", Toast.LENGTH_LONG).show();

                    }
                    break;
            }
        }
            catch(Exception ex)
            {
                ex.printStackTrace();
                DialogHelper.ShowAlert(this, "Error", ex.getMessage(), false);
            }
    }

    private String GetEstimatesString(int weekCounter, int dayCounter, int hourCounter, int minsCounter) {
        String sWeek = "-";
        String sDay = "-";
        String sHour = "-";
        String sMins = "-";
        String estimateString = "";

        try {

            if (weekCounter > 0) {
                sWeek = String.valueOf(weekCounter) + "w" + " ";
            }
            else if(!etWeeks.getText().toString().isEmpty()){
                sWeek = etWeeks.getText()+ "w" + " ";
            }
            if (dayCounter > 0) {
                sDay = String.valueOf(dayCounter) + "d" + " ";
            }
            else if(!etDays.getText().toString().isEmpty()){
                sDay = etDays.getText()+ "d" + " ";
            }
            if (hourCounter > 0) {
                sHour = String.valueOf(hourCounter) + "h" + " ";
            }
            else if(!etHours.getText().toString().isEmpty()){
                sHour = etHours.getText()+ "h" + " ";
            }
            if (minsCounter > 0) {
                sMins = String.valueOf(minsCounter) + "m" + " ";
            }
            else if(!etMins.getText().toString().isEmpty()){
                sMins = etMins.getText()+ "m" + " ";
            }

            estimateString = sWeek + sDay + sHour + sMins;

            if(estimateString.contains("-")) {
                estimateString = estimateString.replace("-", "").trim();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return estimateString;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }

        return true;
    }

}

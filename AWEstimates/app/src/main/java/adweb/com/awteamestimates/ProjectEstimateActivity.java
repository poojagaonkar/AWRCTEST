package adweb.com.awteamestimates;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Collections2;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import adweb.com.awteamestimates.Models.CurrentEstimatedIssue;
import adweb.com.awteamestimates.Models.EstimateModel;
import adweb.com.awteamestimates.Models.ProjectModel;
import adweb.com.awteamestimates.Service.JiraServices;
import adweb.com.awteamestimates.Utilities.AppConstants;

public class ProjectEstimateActivity extends AppCompatActivity implements View.OnClickListener {

    private TableLayout tableFooterLayout;
    private Button btnExpandExtimates;
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


    private TextView txtProjectName;
    private TextView txtIssueTitle;
    private TableRow tableRowWeek, tableRowDays, tableRowHours, tableRowMin;
    private Button btnSubmit;
    private Iterator<CurrentEstimatedIssue> issueDetails;
    private String mIssueKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_estimate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tableFooterLayout = (TableLayout) findViewById(R.id.tableSlidingFooter);
        btnExpandExtimates = (Button) findViewById(R.id.btnEstimates);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnAddWeeks = findViewById(R.id.btnCounterUpW);
        btnRemoveWeeks = findViewById(R.id.btnCounterDownW);
        btnAddDays = findViewById(R.id.btnCounterUpD);
        btnRemoveDays = findViewById(R.id.btnCounterDownD);
        btnAddHours = findViewById(R.id.btnCounterUpH);
        btnRemoveHours = findViewById(R.id.btnCounterDownH);
        btnAddMins = findViewById(R.id.btnCounterUpM);
        btnRemoveMins = findViewById(R.id.btnCounterDownM);

        tableRowWeek = findViewById(R.id.tableRowWeek);
        etWeeks = (EditText) tableRowWeek.getChildAt(1);
        tableRowDays = findViewById(R.id.tableRowDays);
        etDays = (EditText) tableRowDays.getChildAt(1);
        tableRowHours = findViewById(R.id.tableRowHours);
        etHours = (EditText) tableRowHours.getChildAt(1);
        tableRowMin = findViewById(R.id.tableRowMins);
        etMins = (EditText) tableRowMin.getChildAt(1);

        txtProjectName = findViewById(R.id.txtProjectName);
        txtIssueTitle = findViewById(R.id.txtIssueTitle);


        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit = mPrefs.edit();


        mBaseUrl = mPrefs.getString(getResources().getString(R.string.pref_baseUrl), null);
        mUserName = mPrefs.getString(getResources().getString(R.string.pref_userName), null);

        tableFooterLayout.setVisibility(View.INVISIBLE);
        btnExpandExtimates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tableFooterLayout.getVisibility() == View.INVISIBLE) {
                    // Prepare the View for the animation
                    tableFooterLayout.setVisibility(View.VISIBLE);
                    tableFooterLayout.setAlpha(0.0f);

                    // Start the animation
                    tableFooterLayout.animate()
                            .translationY(20)
                            .alpha(1.0f)
                            .setListener(null);


                } else {

                    tableFooterLayout.animate().translationY(0);
                    tableFooterLayout.setVisibility(View.INVISIBLE);

                }
            }
        });

       issueDetails = Collections2.filter(AppConstants.FullProjectList, user -> user.getProjectName().equals(AppConstants.CurrentSelectedProject)).iterator();
       CurrentEstimatedIssue currentIssue = issueDetails.next();
        txtIssueTitle.setText(currentIssue.getIssueTitle());
        txtProjectName.setText(AppConstants.CurrentSelectedProject);
        mIssueKey = currentIssue.getIssueKey();

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

                    String mEstimateString = GetEstimatesString(weekCounter, dayCounter, hourCounter, minsCounter);
              try {

                  JiraServices. SubmitEstimateTask postProjectEstimate = new JiraServices.SubmitEstimateTask(mUserName,mBaseUrl, mEstimateString, mIssueKey);
                  EstimateModel mModel = postProjectEstimate.execute().get();

                  if(mModel !=null && mModel.getSuccess() == 200)
                  {
                      Toast.makeText(this,"Estimate submitted successfully", Toast.LENGTH_LONG).show();
                  }

                  break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            if (dayCounter > 0) {
                sDay = String.valueOf(dayCounter) + "d" + " ";
            }
            if (hourCounter > 0) {
                sHour = String.valueOf(hourCounter) + "h" + " ";
            }
            if (minsCounter > 0) {
                sMins = String.valueOf(minsCounter) + "m" + " ";
            }

            estimateString = sWeek + sDay + sHour + sMins;
            estimateString = estimateString.replace("-", "");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return estimateString;

    }

}

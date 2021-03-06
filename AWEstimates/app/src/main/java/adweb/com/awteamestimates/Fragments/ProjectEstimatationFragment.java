package adweb.com.awteamestimates.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Collections2;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import adweb.com.awteamestimates.HomeActivity;
import adweb.com.awteamestimates.IssueDetailsActivity;
import adweb.com.awteamestimates.Models.CurrentEstimatedIssue;
import adweb.com.awteamestimates.Models.EstimateModel;
import adweb.com.awteamestimates.Models.GetRoles.RoleIdModel;
import adweb.com.awteamestimates.Models.ProjectModel;
import adweb.com.awteamestimates.R;
import adweb.com.awteamestimates.Service.JiraServices;
import adweb.com.awteamestimates.Utilities.AppConstants;
import adweb.com.awteamestimates.Utilities.DialogHelper;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectEstimatationFragment extends Fragment implements View.OnClickListener{

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
    private TextView txtMoreDetails;
    private TextView txtIssueEstimate;
    private TextView txtRoleTitle;
    private EstimateModel estimateModel;
    private JiraServices.SubmitEstimateTask postProjectEstimate;
    private String mEstimateString = "N/A";
    private Collection<RoleIdModel> mRoleIdCollection;
    private RoleIdModel mCurrentRole;
    private String roleTitle ="";
    private ScrollView scrollView;

    public ProjectEstimatationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_project_estimate, container, false);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tableFooterLayout = (TableLayout) view.findViewById(R.id.tableSlidingFooter);
        btnExpandExtimates = (Button) view.findViewById(R.id.btnEstimates);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        btnAddWeeks = view.findViewById(R.id.btnCounterUpW);
        btnRemoveWeeks = view.findViewById(R.id.btnCounterDownW);
        btnAddDays = view.findViewById(R.id.btnCounterUpD);
        btnRemoveDays = view.findViewById(R.id.btnCounterDownD);
        btnAddHours = view.findViewById(R.id.btnCounterUpH);
        btnRemoveHours = view.findViewById(R.id.btnCounterDownH);
        btnAddMins = view.findViewById(R.id.btnCounterUpM);
        btnRemoveMins = view.findViewById(R.id.btnCounterDownM);

        tableRowWeek = view.findViewById(R.id.tableRowWeek);
        etWeeks = (EditText) tableRowWeek.getChildAt(1);
        tableRowDays = view.findViewById(R.id.tableRowDays);
        etDays = (EditText) tableRowDays.getChildAt(1);
        tableRowHours = view.findViewById(R.id.tableRowHours);
        etHours = (EditText) tableRowHours.getChildAt(1);
        tableRowMin = view.findViewById(R.id.tableRowMins);
        etMins = (EditText) tableRowMin.getChildAt(1);

        txtProjectName = view.findViewById(R.id.txtProjectName);
        txtIssueTitle = view.findViewById(R.id.txtIssueTitle);
        txtIssueEstimate = view.findViewById(R.id.txtIssueEstimate);
        txtRoleTitle = view.findViewById(R.id.txtRoleTitle);
        txtMoreDetails = view.findViewById(R.id.txtMoreDetails);
        scrollView = view.findViewById(R.id.scrollView);

        Toolbar mToolbar = ((HomeActivity)getActivity()).toolbar;
        mToolbar.findViewById(R.id.btnProjectNext).setVisibility(View.INVISIBLE);


        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
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

                    scrollView.post(new Runnable() {
                        public void run() {
                            scrollView.scrollTo(0, scrollView.getBottom());
                        }
                    });

                } else {

                    tableFooterLayout.animate().translationY(0);
                    tableFooterLayout.setVisibility(View.INVISIBLE);

                }
            }
        });

        if(AppConstants.isRefreshed)
        {
            ReloadProjectData();

            LayoutInflater layoutInflater =(LayoutInflater)getActivity().getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.role_selection_alert_layout, null);

            AlertDialog.Builder mbuild = new AlertDialog.Builder(getActivity());
            mbuild.setView(popupView);
            Button btnDismiss = (Button)popupView.findViewById(R.id.btnOK);

            Spinner popupSpinner = (Spinner)popupView.findViewById(R.id.rolepopupspinner);
            ArrayAdapter<String> adapter =new ArrayAdapter<String>(getActivity() ,android.R.layout.simple_spinner_item, AppConstants.ProjectRoleTitles);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            popupSpinner.setAdapter(adapter);

            popupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    mCurrentRole.setRoleName(adapter.getItem(i));
                    RoleIdModel m =  AppConstants.ProjectRoleList.get(i);
                    mCurrentRole.setRoleID(m.getRoleID());
                    mCurrentRole.setRoleEstimatee(m.getRoleEstimate());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            mbuild.setTitle("Select role");
            AlertDialog mDialog = mbuild.create();
            btnDismiss.setOnClickListener(new Button.OnClickListener(){

                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    txtRoleTitle.setText(mCurrentRole.getRoleName());

                }});

            mDialog.show();

        }

        txtIssueTitle.setText(AppConstants.CurrentEstimatedIssue.getIssueTitle());
        txtProjectName.setText(AppConstants.CurrentSelectedProject);
        mIssueKey = AppConstants.CurrentEstimatedIssue.getIssueKey();
        if(mRoleIdCollection == null) {
            mRoleIdCollection = Collections2.filter(AppConstants.ProjectRoleList, val -> val.getRoleName().equals(AppConstants.CurrentSelectedRole));
            mCurrentRole = mRoleIdCollection.iterator().next();
        }

        txtIssueEstimate.setText(mCurrentRole.getRoleEstimate());
        if(roleTitle.isEmpty()) {
            roleTitle = AppConstants.CurrentSelectedRole != null ? AppConstants.CurrentSelectedRole.toString() : "Administrator";
            //txtIssueEstimate.setText(mEstimateString);

        }
        txtRoleTitle.setText(roleTitle);


        btnAddWeeks.setOnClickListener(this);
        btnAddHours.setOnClickListener(this);
        btnAddDays.setOnClickListener(this);
        btnAddMins.setOnClickListener(this);

        btnRemoveWeeks.setOnClickListener(this);
        btnRemoveHours.setOnClickListener(this);
        btnRemoveDays.setOnClickListener(this);
        btnRemoveMins.setOnClickListener(this);

        btnSubmit.setOnClickListener(this);
        txtMoreDetails.setOnClickListener(this);

        KeyboardVisibilityEvent.setEventListener(
                getActivity(),
                isOpen -> {

                    scrollView.post(new Runnable() {
                        public void run() {
                            scrollView.scrollTo(0, scrollView.getBottom());
                        }
                    });
                });
    }

    private void ReloadProjectData() {

        //region Get Project Details
        try {

            if(AppConstants.ProjectTitles == null || AppConstants.ProjectTitles.size() ==0 || AppConstants.isRefreshed ) {
                JiraServices.GetProjectDetails getProjectDetails = new JiraServices.GetProjectDetails(getActivity(), mUserName, mBaseUrl);
                ProjectModel mModel = getProjectDetails.execute().get();

                AppConstants.isRefreshed = false;

                if (mModel != null) {


                    AppConstants.FullProjectList = mModel.getCurrentEstimatedIssue();
                    AppConstants.ProjectTitles = new ArrayList<>();



                    for (CurrentEstimatedIssue mIssue : AppConstants.FullProjectList) {

                        AppConstants.ProjectTitles.add(mIssue.getProjectName());
                        System.out.println(mIssue.getIssueKey() + "\n" + mIssue.getIssueTitle() + "\n" + mIssue.getProjectKey() + "\n" + mIssue.getProjectName());
                        //AppConstants.CurrentSelectedProject =adapter.getItem(i);
                        AppConstants.CurrentIssueDetails = Collections2.filter(AppConstants.FullProjectList, user -> user.getProjectName().equals(AppConstants.CurrentSelectedProject)).iterator();
                        AppConstants.CurrentEstimatedIssue  = AppConstants.CurrentIssueDetails.next();

                    }
                }
                else
                {
                    throw new RuntimeException("Could not fetch project details.");
                }
            }




        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        catch (Exception ex)
        {
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        //endregion

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

                                 postProjectEstimate = new JiraServices.SubmitEstimateTask(getActivity(), mUserName, mBaseUrl, mEstimateString, mIssueKey);
                            }
                            else
                            {
                                postProjectEstimate = new JiraServices.SubmitEstimateTask(getActivity(), mUserName, mBaseUrl, mEstimateString, mIssueKey , mCurrentRole.getRoleID(), mCurrentRole.getRoleName());

                            }


                            estimateModel = postProjectEstimate.execute().get();

                            if (estimateModel != null && estimateModel.getSuccess() == 200) {

                                DialogHelper.ShowAlert(getActivity(), "Success", "Estimate submitted successfully");
                                etWeeks.setText("");
                                etDays.setText("");
                                etHours.setText("");
                                etMins.setText("");
                                tableFooterLayout.setVisibility(View.INVISIBLE);
                                btnExpandExtimates.setText("Update Estimation");
                                txtIssueEstimate.setText(mEstimateString);
                                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                            else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            DialogHelper.ShowAlert(getActivity(), "Error", e.getMessage());
                        }

                    } else {

                    }
                    break;
                case R.id.txtMoreDetails:
                //loadFragment(new MoreIssuesFragment());
                    getActivity().startActivity(new Intent(getActivity(), IssueDetailsActivity.class));
                    break;

            }
        }catch (Exception e) {
            e.printStackTrace();
        }


    }
    private void loadFragment(Fragment fragment) {
        // create a FragmentManager
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
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

}

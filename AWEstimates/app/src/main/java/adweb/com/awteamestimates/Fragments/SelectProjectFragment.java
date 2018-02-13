package adweb.com.awteamestimates.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import adweb.com.awteamestimates.HomeActivity;
import adweb.com.awteamestimates.Models.CurrentEstimatedIssue;
import adweb.com.awteamestimates.Models.ProjectModel;
import adweb.com.awteamestimates.R;
import adweb.com.awteamestimates.Service.JiraServices;
import adweb.com.awteamestimates.Utilities.AppConstants;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectProjectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectProjectFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TextView txtTemp;
    private Spinner mProjectSpinner;
    private ImageButton btnNext;
    public SharedPreferences mPrefs ;
    public  SharedPreferences.Editor mEdit ;
    private  String mUserName ;
    private  String mSessionUserName;
    private  String mSessionUserValue;
    private  String mBaseUrl;
    private boolean isMenuVisible;
    private Button btnProjectNext;
    private Spinner mRoleSpinner;
    private LinearLayout layoutRole;

    public SelectProjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectProjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectProjectFragment newInstance(String param1, String param2) {
        SelectProjectFragment fragment = new SelectProjectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEdit = mPrefs.edit();
        setHasOptionsMenu(true);
        //setRetainInstance(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.select_project_layout, null, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtTemp = (TextView)view.findViewById(R.id.txtSelect);
        mProjectSpinner = (Spinner)view.findViewById(R.id.spinProjectName);
        mRoleSpinner = (Spinner)view.findViewById(R.id.spinRoleName);
        layoutRole = (LinearLayout)view.findViewById(R.id.layoutRole);

        mBaseUrl = mPrefs.getString(getResources().getString(R.string.pref_baseUrl), null);
        mUserName = mPrefs.getString(getResources().getString(R.string.pref_userName), null);

        Toolbar mtoolbar = ((HomeActivity)getActivity()).toolbar;
        btnProjectNext = (Button) mtoolbar.findViewById(R.id.btnProjectNext);



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

                    }
                }
                else
                {
                    throw new RuntimeException("Could not fetch project details.");
                }
            }

                final ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,   AppConstants.ProjectTitles);
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

                btnProjectNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        loadFragment(new ProjectEstimatationFragment());
//                        Intent mIntent = new Intent();
//                        mIntent.setClass(getActivity(), ProjectEstimateActivity.class);
//                        startActivity(mIntent);
                    }
                });


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
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem menuItem = menu.findItem(R.id.action_refresh);
        menuItem.setVisible(false);


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

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        try {
            super.setMenuVisibility(menuVisible);
            if (menuVisible) {
                isMenuVisible  = menuVisible;
            }
        } catch (Exception e) {

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

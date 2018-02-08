package adweb.com.awteamestimates.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import adweb.com.awteamestimates.Models.CurrentEstimatedIssue;
import adweb.com.awteamestimates.Models.ProjectModel;
import adweb.com.awteamestimates.ProjectEstimateActivity;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.select_project_layout, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtTemp = (TextView)view.findViewById(R.id.txtSelect);
        mProjectSpinner = (Spinner)view.findViewById(R.id.spinProjectName);
        btnNext =(ImageButton)view.findViewById(R.id.btnNext);

        mBaseUrl = mPrefs.getString(getResources().getString(R.string.pref_baseUrl), null);
        mUserName = mPrefs.getString(getResources().getString(R.string.pref_userName), null);

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

                final ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, projectTitles);
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
                        mIntent.setClass(getActivity(), ProjectEstimateActivity.class);
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
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        //endregion


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

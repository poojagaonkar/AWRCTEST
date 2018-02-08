package adweb.com.awteamestimates.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import adweb.com.awteamestimates.Adapters.MoreDetailsAdapter;
import adweb.com.awteamestimates.Models.MoreDetailModel;
import adweb.com.awteamestimates.R;
import adweb.com.awteamestimates.Service.JiraServices;
import adweb.com.awteamestimates.Utilities.AppConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreIssuesFragment extends Fragment {


    private ListView lvMoreList;
    public SharedPreferences mPrefs ;
    public  SharedPreferences.Editor mEdit ;
    private  String mUserName ;
    private  String mBaseUrl;


    public MoreIssuesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       return  inflater.inflate(R.layout.more_details_layout, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEdit = mPrefs.edit();

        mBaseUrl = mPrefs.getString(getResources().getString(R.string.pref_baseUrl), null);
        mUserName = mPrefs.getString(getResources().getString(R.string.pref_userName), null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        try {
            JiraServices.GetMoreDetails mauth= new JiraServices.GetMoreDetails(mUserName, mBaseUrl, AppConstants.CurrentEstimatedIssue.getIssueKey());

            MoreDetailModel mModel = mauth.execute().get();

            if(mModel !=null)
            {

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        lvMoreList = (ListView)view.findViewById(R.id.lvIssueDetails);


        lvMoreList.setAdapter(new MoreDetailsAdapter(getActivity()));
    }
}

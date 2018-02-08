package adweb.com.awteamestimates.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Map;

import adweb.com.awteamestimates.Adapters.MoreDetailsAdapter;
import adweb.com.awteamestimates.R;
import adweb.com.awteamestimates.Utilities.AppConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreIssuesFragment extends Fragment {


    private ListView lvMoreList;

    public MoreIssuesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       return  inflater.inflate(R.layout.more_details_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lvMoreList = (ListView)view.findViewById(R.id.lvIssueDetails);


        lvMoreList.setAdapter(new MoreDetailsAdapter(getActivity()));
    }
}

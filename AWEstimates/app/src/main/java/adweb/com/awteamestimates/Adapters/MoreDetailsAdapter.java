package adweb.com.awteamestimates.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;
import java.util.zip.Inflater;

import adweb.com.awteamestimates.Models.CurrentEstimatedIssue;
import adweb.com.awteamestimates.R;
import adweb.com.awteamestimates.Utilities.AppConstants;

/**
 * Created by PoojaGaonkar on 2/8/2018.
 */

public class MoreDetailsAdapter extends BaseAdapter {


    private final Activity mActivity;
    private TextView txtFieldName;
    private TextView txtIssueDetail;
    private String mCurrent;
    private  ArrayList mData;


    public  MoreDetailsAdapter(Activity mActivity)
    {
        this.mActivity = mActivity;
        mData = new ArrayList();
        mData.addAll(AppConstants.CurrentProjectDetailMap.entrySet());
    }

    @Override
    public int getCount() {

        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return (Map.Entry) mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        View mView = mActivity.getLayoutInflater().inflate(R.layout.more_issues_row , viewGroup, false);
        txtFieldName = (TextView)mView.findViewById(R.id.txtFieldName);
        txtIssueDetail  = (TextView)mView.findViewById(R.id.txtIssueDetail);

        Map.Entry<String, String> item = (Map.Entry<String, String>) getItem(i);

//        String fieldName = String.valueOf();
//        String issueName = String.valueOf();
        txtFieldName.setText(item.getKey());
        txtIssueDetail.setText(item.getValue());
        return mView;
    }
}

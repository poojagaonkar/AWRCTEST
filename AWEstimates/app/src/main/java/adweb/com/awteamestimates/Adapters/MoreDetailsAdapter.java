package adweb.com.awteamestimates.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmadrosid.svgloader.SvgLoader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import adweb.com.awteamestimates.Models.CurrentEstimatedIssue;
import adweb.com.awteamestimates.Models.MoreIssueDetailModel;
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

    }

    @Override
    public int getCount() {

        return AppConstants.CurrentProjectDetailList.size();
    }

    @Override
    public Object getItem(int i) {
        return (AppConstants.CurrentProjectDetailList.get(i));
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
        ImageView issueIcon = (ImageView) mView.findViewById(R.id.issueIcon);

        MoreIssueDetailModel item = AppConstants.CurrentProjectDetailList.get(i);

//        String fieldName = String.valueOf();
//        String issueName = String.valueOf();
        txtFieldName.setText(item.getFieldTitle());
        txtIssueDetail.setText(item.getFieldValue());

        String iconUrl = AppConstants.BaseUrl  + AppConstants.CurrentEstimatedIssue.getAvatarIssue();
        String strToMatch = "Issue Type :  ";
        String mFirled = item.getFieldTitle();

        if(mFirled.matches(strToMatch))
        {

            SvgLoader.pluck()
                    .with(mActivity)
                    .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                    .load(iconUrl, issueIcon);



        }
        return mView;
    }
}

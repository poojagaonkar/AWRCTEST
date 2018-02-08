package adweb.com.awteamestimates.Utilities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;

import adweb.com.awteamestimates.R;

/**
 * Created by PoojaGaonkar on 2/8/2018.
 */

public class DialogHelper {

    private static Dialog dialog;

    public static void ShowProgressDialog(Activity mActivity, boolean isDismissed, String progressMessage)
    {

        try {
            if (!isDismissed == true)
            {
                dialog = new Dialog(mActivity, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
                dialog.setContentView(R.layout.custom_progress_layout);

                dialog.setCancelable(false);
                dialog.show();
            }
            else
                dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  static void ShowAlert(Activity mActivity, String title, String message)
    {
        AlertDialog.Builder mAlert = new AlertDialog.Builder(mActivity, R.style.CustomDialog);
        mAlert.setTitle(title);
        mAlert.setMessage(message);
        mAlert.setPositiveButton("Ok", null);
        mAlert.create().show();
    }


}

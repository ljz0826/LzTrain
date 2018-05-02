package com.lz.lztrain.utils;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.lz.lztrain.R;
import com.lz.lztrain.view.CustomProgressDialog;

/**
 * Created by lenovo on 2017/6/30.
 */

public class TrainApplication extends Application{

    private static Context mContext;
    private CustomProgressDialog mProgressDialog;
    private static TrainApplication application;

    public static TrainApplication getCoreApplication() {
        return application;
    }

    public TrainApplication() {
        super();
        application = this;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Fresco.initialize(this);
    }

    public static Context getInstance() {
        return mContext;
    }

    public ProgressDialog showProgressDialog(Context context) {
        if (mProgressDialog == null) {
            CustomProgressDialog dialog = new CustomProgressDialog(context, R.style.custom_dialog);
            dialog.setMessage("loading_data_please_wait");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            mProgressDialog = dialog;
        }
        try {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mProgressDialog;
    }

    public void dismissProgressDialog() {
        try {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            mProgressDialog = null;
        } catch (Exception e) {
            // We don't mind. android cleared it for us.
            mProgressDialog = null;
        }
    }

}

package com.lz.mylibrary;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;


/**
 * Created by ljz on 2015/11/1.
 */
public class BaseActivity extends AppCompatActivity {

    private Dialog dialog;
    public boolean hasRequest=false;

    public AppBarLayout mAppbarLayout;
    public Toolbar mToolbar;
    public TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dialog = MyProgressDialog.createDialog(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initToolbar();
    }

    private void initToolbar() {
        mAppbarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) findViewById(R.id.title);
//        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        dialog.dismiss();
        // if (netStatus != null)
        super.onDestroy();
    }

    public void showProgress() {
        if (!dialog.isShowing())
            dialog.show();
    }

    public void closeProgress() {
        if (dialog.isShowing())
            dialog.dismiss();
    }


}

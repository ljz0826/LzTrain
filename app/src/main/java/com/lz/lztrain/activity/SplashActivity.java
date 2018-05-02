package com.lz.lztrain.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.lz.lztrain.R;
import com.lz.lztrain.utils.PrefsUtil;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by ljz on 2015/11/2.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final String name = PrefsUtil.getPrefs().getString(PrefsUtil.SP_NAME,"");
        final String number = PrefsUtil.getPrefs().getString(PrefsUtil.SP_NUM,"");
        final String position = PrefsUtil.getPrefs().getString(PrefsUtil.SP_POSITION,"");
        final String department = PrefsUtil.getPrefs().getString(PrefsUtil.SP_DEPARTMENT,"");
        if(name!=null&& !TextUtils.isEmpty(name)){
            final Intent localIntent = new Intent(this, MainActivity.class);
            Timer timer = new Timer();
            TimerTask tast = new TimerTask() {
                @Override
                public void run() {
                    localIntent.putExtra("name",name);
                    localIntent.putExtra("number",number);
                    localIntent.putExtra("position",position);
                    localIntent.putExtra("department",department);
                    startActivity(localIntent);
                    finish();
                }
            };
            timer.schedule(tast, 3000);
        }else{
            final Intent localIntent = new Intent(this, LoginActivity.class);
            Timer timer = new Timer();
            TimerTask tast = new TimerTask() {
                @Override
                public void run() {
                    startActivity(localIntent);
                    finish();
                }
            };
            timer.schedule(tast, 3000);
        }
    }
}


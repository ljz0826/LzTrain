package com.lz.lztrain.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by lenovo on 2017/7/4.
 */

public class PermissionUtil {

    public static final int REQUEST_CODE_CAMERA = 1; // 申请相机权限

    /**
     * Activity中申请相机权限
     *
     * @param activity
     * @return
     */
    public static boolean requestCameraPermissionFromActivity(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
            return false;
        } else {
            return true;
        }
    }

}

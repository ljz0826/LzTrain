package com.lz.mylibrary;

import android.app.Dialog;
import android.content.DialogInterface;
import android.view.KeyEvent;


/**
 * Created by Administrator on 2015/12/1.
 */
public class MyProgressDialog {
    /**
     * 创建全局进度条
     */
    public static Dialog createDialog(final BaseActivity activity) {
        Dialog dialog =  new Dialog(activity, R.style.waiting_progress_dialog);
        dialog.setContentView(R.layout.waiting_process_dialog);
        dialog.setCancelable(false);
//		dialog.setMessage("加载中...");
        dialog.setTitle( null );
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dialog.dismiss();
                }
                return false;
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });
        return dialog;
    }

}

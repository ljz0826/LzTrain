package com.lz.mylibrary;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class TrainToast extends Toast {

    public TrainToast(Context context) {
        super(context);
    }

    private static TrainToast result = null;

    public static Toast makeText(Context context, int resId, int duration) throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

    public static Toast makeText(Context context, CharSequence text, int duration) {
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.music_toast_layout, null);
        v.setBackgroundResource(R.drawable.bg_toast);
        v.setPadding(10, 0, 10, 0);
        TextView textview = (TextView) v.findViewById(R.id.toast_message);
        if (textview != null) {
            textview.setGravity(Gravity.CENTER);
            textview.setText(text);
            if (result == null) {
                result = new TrainToast(context);
            }
            result.setView(v);
            result.setDuration(duration);
        }
        // 修复在程序即将崩溃时因view为空导致的空指针
        if (result == null && context != null) {
            result = new TrainToast(context);
        }
        return result;
    }

}

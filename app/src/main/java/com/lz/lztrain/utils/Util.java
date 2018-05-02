package com.lz.lztrain.utils;

/**
 * Created by lenovo on 2017/6/29.
 */

public class Util {

    private static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

//    if (Utils.isFastDoubleClick()) {
//        return;
//    }
}

package com.yc.ac.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

public class CommonUtils {
    public static boolean isDestory(Activity activity){
        if (activity==null||activity.isFinishing()||(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1&&activity.isDestroyed())){
            return true;
        }else {
            return false;
        }
    }

    public static boolean lacksPermissions(Context mContexts) {
        if (ContextCompat.checkSelfPermission(mContexts, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //没有权限
            return false;
        } else {
            return true;
            //有权限
        }
    }
}

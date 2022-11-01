package com.yc.ac.utils;

import android.app.Activity;
import android.os.Build;

public class CommonUtils {
    public static boolean isDestory(Activity activity){
        if (activity==null||activity.isFinishing()||(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1&&activity.isDestroyed())){
            return true;
        }else {
            return false;
        }
    }
}

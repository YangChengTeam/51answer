package com.yc.ac.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yc.ac.R;


/**
 * TinyHung@outlook.com
 * 2017/3/17 16:56
 */
public class ToastUtils {

    private static TextView sMTv_text;
    private static Toast centerToast;

    public static void showCenterToast(Context context, String text) {
        if (null == centerToast) {
            centerToast = new Toast(context);
            centerToast.setDuration(Toast.LENGTH_LONG);
            centerToast.setGravity(Gravity.NO_GRAVITY, 0, 0);
            View view = View.inflate(context, R.layout.toast_center_layout, null);
            sMTv_text = view.findViewById(R.id.tv_text);
            sMTv_text.setText(TextUtils.isEmpty(text) ? "" : text);
            centerToast.setView(view);
        } else {
            sMTv_text.setText(TextUtils.isEmpty(text) ? "" : text);
        }
        centerToast.show();
    }
}

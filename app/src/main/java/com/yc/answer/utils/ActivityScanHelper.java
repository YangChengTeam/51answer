package com.yc.answer.utils;

import android.content.Context;
import android.content.Intent;

import com.google.zxing.Result;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.activity.ActivityScanerCode;
import com.vondear.rxtools.activity.ActivityScanerCodeNew;
import com.vondear.rxtools.interfaces.OnRxScanerListener;
import com.yc.answer.index.ui.activity.SearchActivity;

/**
 * Created by wanglin  on 2018/3/16 11:08.
 */

public class ActivityScanHelper {
    public static void startScanerCode(final Context context) {
        Intent intent = new Intent(context, ActivityScanerCodeNew.class);
        ActivityScanerCodeNew.setScanerListener(new OnRxScanerListener() {
            @Override
            public void onSuccess(String type, Result result) {
                Intent intent1 = new Intent(context, SearchActivity.class);
                intent1.putExtra("code", result.getText());
                intent1.putExtra("page", 1);
                context.startActivity(intent1);
                RxActivityTool.finishAllActivity();
            }

            @Override
            public void onFail(String type, String message) {
                ToastUtils.showCenterToast(context, message);
            }

            @Override
            public void onInput() {
                Intent intent1 = new Intent(context, SearchActivity.class);
                intent1.putExtra("page", 2);
                context.startActivity(intent1);
                RxActivityTool.finishAllActivity();
            }
        });
        context.startActivity(intent);
    }
}

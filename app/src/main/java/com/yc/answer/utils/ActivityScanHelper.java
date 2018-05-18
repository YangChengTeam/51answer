package com.yc.answer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.zxing.Result;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.activity.ActivityScanerCode;
import com.vondear.rxtools.activity.ActivityScanerCodeNew;
import com.vondear.rxtools.interfaces.OnRxScanerListener;
import com.yc.answer.index.ui.activity.ScanTintActivity;
import com.yc.answer.index.ui.activity.SearchActivity;
import com.yc.answer.index.ui.activity.SearchActivityNew;
import com.yc.answer.index.ui.activity.SearchCodeActivity;

/**
 * Created by wanglin  on 2018/3/16 11:08.
 */

public class ActivityScanHelper {
    public static void startScanerCode(final Context context) {
        Intent intent = new Intent(context, ActivityScanerCodeNew.class);
        ActivityScanerCodeNew.setScanerListener(new OnRxScanerListener() {
            @Override
            public void onSuccess(String type, Result result) {
                Intent intent1 = new Intent(context, SearchActivityNew.class);
                intent1.putExtra("code", result.getText());
                context.startActivity(intent1);
                RxActivityTool.finishAllActivity();
            }

            @Override
            public void onFail(String type, String message) {
                ToastUtils.showCenterToast(context, message);
            }

            @Override
            public void onInput() {
                Intent intent1 = new Intent(context, SearchCodeActivity.class);
                context.startActivity(intent1);
                RxActivityTool.finishAllActivity();
            }

            @Override
            public void onHelpClick() {
                context.startActivity(new Intent(context, ScanTintActivity.class));
                ((Activity) context).finish();
            }
        });
        context.startActivity(intent);
    }
}

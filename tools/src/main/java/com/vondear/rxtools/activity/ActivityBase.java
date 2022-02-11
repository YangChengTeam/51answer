package com.vondear.rxtools.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.zxing.Result;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.module.scaner.CaptureActivityHandler;

import androidx.fragment.app.FragmentActivity;

/**
 * @author vondear
 */
public class ActivityBase extends FragmentActivity {

    public ActivityBase mContext;
    protected CaptureActivityHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        RxActivityTool.addActivity(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public Handler getHandler() {
        return handler;
    }


    public void handleDecode(Result obj) {

    }
}

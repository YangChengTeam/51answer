package com.yc.answer.index.ui.activity;

import android.content.Intent;

import com.yc.answer.R;
import com.yc.answer.base.MainActivity;

import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/3/15 14:11.
 */

public class SplashActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void init() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 500);
    }


}

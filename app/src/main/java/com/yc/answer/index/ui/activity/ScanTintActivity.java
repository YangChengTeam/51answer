package com.yc.answer.index.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.vondear.rxtools.RxDeviceTool;
import com.yc.answer.R;
import com.yc.answer.utils.ActivityScanHelper;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/3/22 09:38.
 */

public class ScanTintActivity extends BaseActivity {
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;
    @BindView(R.id.iv_know)
    ImageView ivKnow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_tint;
    }

    @Override
    public void init() {
        commonTvTitle.setText(getString(R.string.scan_tint));
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });

        RxView.clicks(ivKnow).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ActivityScanHelper.startScanerCode(ScanTintActivity.this);
                finish();
            }
        });


//        Glide.with(this).load(R.mipmap.scan_tint_bg).into(ivBg);
    }


}

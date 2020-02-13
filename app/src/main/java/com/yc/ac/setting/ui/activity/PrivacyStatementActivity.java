package com.yc.ac.setting.ui.activity;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.ac.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.BasePresenter;

public class PrivacyStatementActivity extends BaseActivity<BasePresenter> {


    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;


    @Override
    public int getLayoutId() {
        return R.layout.activity_privacy_statement;
    }

    @Override
    public void init() {

        commonTvTitle.setText("隐私声明");
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> finish());
    }


}

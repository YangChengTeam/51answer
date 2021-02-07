package com.yc.ac.setting.ui.activity;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.ac.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import yc.com.base.BaseActivity;
import yc.com.base.BasePresenter;

public class UserPolicyActivity extends BaseActivity<BasePresenter> {


    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;


    @Override
    public int getLayoutId() {
        return R.layout.activity_user_policy;
    }

    @Override
    public void init() {

        commonTvTitle.setText("用户协议");
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> finish());
    }


}

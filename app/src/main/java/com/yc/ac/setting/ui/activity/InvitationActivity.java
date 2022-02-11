package com.yc.ac.setting.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.ac.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/5/21 11:19.
 */

public class InvitationActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;

    @BindView(R.id.et_invitation_code)
    EditText etInvitationCode;
    @BindView(R.id.btn_activate)
    Button btnActivate;

    @Override
    public int getLayoutId() {
        return R.layout.activity_invitation_code;
    }

    @Override
    public void init() {
        commonTvTitle.setText(getString(R.string.simple_invitation_code));
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });


    }


}

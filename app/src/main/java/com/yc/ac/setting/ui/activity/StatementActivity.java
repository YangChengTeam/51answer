package com.yc.ac.setting.ui.activity;

import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.ac.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/3/23 12:06.
 */

public class StatementActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;

    @Override
    public int getLayoutId() {
        return R.layout.activity_statement;
    }

    @Override
    public void init() {
        commonTvTitle.setText(getString(R.string.statement));
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
    }


}

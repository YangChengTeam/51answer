package com.yc.answer.index.ui.activity;

import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.answer.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/4/23 14:01.
 */

public class ForwardActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;
    @BindView(R.id.tv_tips)
    TextView tvTips;

    @Override
    public int getLayoutId() {
        return R.layout.activity_forward;
    }

    @Override
    public void init() {

        commonTvTitle.setText(getString(R.string.forward_explain));
        tvTips.setText("小技巧");
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });

    }



}

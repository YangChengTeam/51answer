package com.yc.ac.index.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yc.ac.R;
import com.yc.ac.utils.ToastUtils;

import butterknife.BindView;
import yc.com.base.BaseActivity;

public class PaySuccessActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;
    @BindView(R.id.tv_sure)
    TextView tvSure;

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_success;
    }

    @Override
    public void init() {
        commonTvTitle.setText(getString(R.string.app_name));
        initListener();
    }

    private void initListener() {
        ivBack.setOnClickListener(v -> finish());
        tvSure.setOnClickListener(v -> ToastUtils.showCenterToast(PaySuccessActivity.this, "支付成功"));
    }


}

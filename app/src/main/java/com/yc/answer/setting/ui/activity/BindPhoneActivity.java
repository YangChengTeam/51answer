package com.yc.answer.setting.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.answer.R;
import com.yc.answer.setting.contract.BindPhoneContract;
import com.yc.answer.setting.presenter.BindPhonePresenter;
import com.yc.answer.utils.UserInfoHelper;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/3/15 10:13.
 */

public class BindPhoneActivity extends BaseActivity<BindPhonePresenter> implements BindPhoneContract.View {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.iv_account_cancel)
    ImageView ivAccountCancel;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private String flag;

    private Animation mInputAnimation;
    private boolean isChange;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_phone;
    }

    @Override
    public void init() {
        mPresenter = new BindPhonePresenter(this, this);
        flag = getIntent().getStringExtra("flag");
        commonTvTitle.setText(flag + "手机号");
        if ("修改".equals(flag)) {
            isChange = true;
        }
        etAccount.setText(UserInfoHelper.getUserInfo().getMobile());
        if (!TextUtils.isEmpty(UserInfoHelper.getUserInfo().getMobile()))
            etAccount.setSelection(UserInfoHelper.getUserInfo().getMobile().length());
        mInputAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
        initListener();
    }

    private void initListener() {
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        RxView.clicks(ivAccountCancel).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                etAccount.setText("");
            }
        });

        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ivAccountCancel.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        RxView.clicks(btnSubmit).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String phone = etAccount.getText().toString().trim();
                String code = etCode.getText().toString().trim();
                if (isChange) {
                    mPresenter.changePhone(phone, code);
                } else {
                    mPresenter.bindPhone(phone, code);
                }


            }
        });
        RxView.clicks(tvGetCode).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String phone = etAccount.getText().toString().trim();
                mPresenter.getCode(phone);
            }
        });

    }

    @Override
    public void showErrorAccount() {
        etAccount.startAnimation(mInputAnimation);
    }

    @Override
    public void showErrorCode() {
        etCode.startAnimation(mInputAnimation);
    }

    @Override
    public void showGetCode() {
        showGetCodeDisplay(tvGetCode);
    }
}

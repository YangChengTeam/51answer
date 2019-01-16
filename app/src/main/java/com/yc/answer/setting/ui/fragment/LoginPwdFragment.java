package com.yc.answer.setting.ui.fragment;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.yc.answer.R;
import com.yc.answer.setting.contract.LoginContract;
import com.yc.answer.setting.model.bean.UserInfo;
import com.yc.answer.setting.presenter.LoginPresenter;
import com.yc.answer.setting.ui.activity.LoginGroupActivityNew;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseFragment;

/**
 * Created by wanglin  on 2019/1/5 11:12.
 */
public class LoginPwdFragment extends BaseFragment<LoginPresenter> implements LoginContract.View {


    @BindView(R.id.tv_forget_pwd)
    TextView tvForgetPwd;
    @BindView(R.id.tv_code_login)
    TextView tvCodeLogin;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.iv_cancel)
    ImageView ivCancel;


    private String phone;
    private LoginGroupActivityNew mLoginGroupActivity;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login_pwd;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLoginGroupActivity = (LoginGroupActivityNew) context;
    }

    @Override
    public void init() {

        if (getArguments() != null) {
            phone = getArguments().getString("phone");
        }
        mPresenter = new LoginPresenter(getActivity(), this);


        etPwd.addTextChangedListener(accountChangeListener);

        //监听焦点获悉情况
        etPwd.setOnFocusChangeListener(onFocusChangeListener);

        initListener();
    }

    private void initListener() {
        RxView.clicks(tvLogin).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String pwd = etPwd.getText().toString().trim();
                mPresenter.login(phone, pwd, "");
            }
        });

        RxView.clicks(ivCancel).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                etPwd.setText("");
                etPwd.setHint("请输入密码登录");
                ivCancel.setVisibility(View.INVISIBLE);
                tvLogin.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.login_btn_bg_normal));
            }
        });

        RxView.clicks(tvForgetPwd).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mLoginGroupActivity.addReplaceFragment(LoginGroupActivityNew.FORGET_PWD_CODE, "登录", "注册", phone);
            }
        });

        RxView.clicks(tvCodeLogin).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mLoginGroupActivity.addReplaceFragment(LoginGroupActivityNew.LOGIN_CODE, "登录", "注册", phone);
            }
        });
    }

    /**
     * 账号输入框监听
     */
    private TextWatcher accountChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (null != ivCancel)
                ivCancel.setVisibility(!TextUtils.isEmpty(s) && s.length() > 0 ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().trim().length() > 0) {
                tvLogin.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.login_btn_bg_select));
            } else {
                tvLogin.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.login_btn_bg_normal));
            }
        }
    };

    /**
     * 对个输入框焦点进行监听
     */
    private View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()) {
                case R.id.et_account:
                    if (hasFocus) {
                        if (etPwd.getText().toString().length() > 0) {
                            ivCancel.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (null != ivCancel) ivCancel.setVisibility(View.INVISIBLE);
                    }
                    break;
            }
        }
    };


    @Override
    public void showErrorAccount() {

    }

    @Override
    public void showErrorPassword() {

    }

    @Override
    public void showErrorCode() {

    }

    @Override
    public void showGetCode() {

    }

    @Override
    public void showAccountResult(UserInfo userInfo, String string) {

    }

    @Override
    public void showLoadingDialog(String mess) {
        if (null != mLoginGroupActivity)
            mLoginGroupActivity.showLoadingDialog(mess);
    }

    @Override
    public void dismissDialog() {
        if (null != mLoginGroupActivity)
            mLoginGroupActivity.dismissDialog();
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

}

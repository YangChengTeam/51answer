package com.yc.ac.setting.ui.fragment;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.yc.ac.R;
import com.yc.ac.setting.contract.LoginContract;
import com.yc.ac.setting.model.bean.UserInfo;
import com.yc.ac.setting.presenter.LoginPresenter;
import com.yc.ac.setting.ui.activity.LoginGroupActivity;

import java.util.concurrent.TimeUnit;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseFragment;

/**
 * Created by wanglin  on 2019/1/5 11:12.
 */
public class LoginNewPwdFragment extends BaseFragment<LoginPresenter> implements LoginContract.View {


    @BindView(R.id.iv_cancel)
    ImageView ivCancel;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_code_login)
    TextView tvCodeLogin;
    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;


    private String phone;
    private String code;
    private LoginGroupActivity mLoginGroupActivity;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login_new_pwd;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLoginGroupActivity = (LoginGroupActivity) context;
    }

    @Override
    public void init() {

        if (getArguments() != null) {
            phone = getArguments().getString("phone");
            code = getArguments().getString("code");
            LogUtil.msg("phone: " + phone + "  code:  " + code);
        }
        mPresenter = new LoginPresenter(getActivity(), this);

        etNewPwd.addTextChangedListener(accountChangeListener);
        //监听焦点获悉情况
        etNewPwd.setOnFocusChangeListener(onFocusChangeListener);
        initListener();
    }

    private void initListener() {
        RxView.clicks(tvLogin).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String newPwd = etNewPwd.getText().toString().trim();
                mPresenter.resetPwd(phone, code, newPwd);
            }
        });


        RxView.clicks(tvCodeLogin).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mLoginGroupActivity.addReplaceFragment(LoginGroupActivity.LOGIN_CODE, "登录", "注册", phone);
            }
        });
        RxView.clicks(ivCancel).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                etNewPwd.setText("");
                etNewPwd.setHint("请输入新密码");
                ivCancel.setVisibility(View.INVISIBLE);
                tvLogin.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.login_btn_bg_normal));
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
                        if (etNewPwd.getText().toString().length() > 0) {
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

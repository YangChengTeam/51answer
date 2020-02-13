package com.yc.ac.setting.ui.fragment;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.ac.R;
import com.yc.ac.setting.contract.LoginContract;
import com.yc.ac.setting.model.bean.UserInfo;
import com.yc.ac.setting.presenter.LoginPresenter;
import com.yc.ac.setting.ui.activity.LoginGroupActivityNew;

import java.util.concurrent.TimeUnit;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseFragment;

/**
 * Created by wanglin  on 2019/1/5 11:12.
 */
public class ForgetPwdCodeFragment extends BaseFragment<LoginPresenter> implements LoginContract.View {


    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.tv_forget_pwd)
    TextView tvForgetPwd;
    @BindView(R.id.tv_code_login)
    TextView tvCodeLogin;

    private String phone;
    private LoginGroupActivityNew mLoginGroupActivity;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_forget_pwd_code;
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

        mPresenter.getCode(phone);

        etCode.addTextChangedListener(accountChangeListener);

        initListener();
    }

    private void initListener() {
        RxView.clicks(tvNext).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String code = etCode.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    return;
                }
                mLoginGroupActivity.addReplaceFragment(LoginGroupActivityNew.LOGIN_NEW_PWD, "登录", "注册", phone, code);
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
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().trim().length() > 0) {
                tvNext.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.login_btn_bg_select));
            } else {
                tvNext.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.login_btn_bg_normal));
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
        mLoginGroupActivity.showGetCodeDisplay(tvGetCode);
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

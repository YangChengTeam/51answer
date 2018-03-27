package com.yc.answer.setting.ui.fragment;

import android.content.Context;
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


import com.yc.answer.R;
import com.yc.answer.setting.contract.LoginContract;
import com.yc.answer.setting.model.bean.UserInfo;
import com.yc.answer.setting.presenter.LoginPresenter;
import com.yc.answer.setting.ui.activity.LoginGroupActivity;
import com.yc.answer.utils.ToastUtils;
import com.yc.answer.utils.UserInfoHelper;

import butterknife.BindView;
import yc.com.base.BaseActivity;
import yc.com.base.BaseFragment;

/**
 * TinyHung@Outlook.com
 * 2017/11/28.
 * 修改密码
 */

public class LoginEditPasswordFragment extends BaseFragment<LoginPresenter> implements LoginContract.View {


    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.iv_account_cancel)
    ImageView ivAccountCancel;
    @BindView(R.id.iv_password_cancel)
    ImageView ivPasswordCancel;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private Animation mInputAnimation;

    private LoginGroupActivity mLoginGroupActivity;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLoginGroupActivity = (LoginGroupActivity) context;
    }


    protected void initViews() {
        mPresenter = new LoginPresenter(getActivity(), this);
        mInputAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    //获取验证码
                    case R.id.tv_get_code:
                        cureateGetNumberCode();
                        break;
                    //确定修改
                    case R.id.btn_submit:
                        cureateSubmit();
                        break;
                    //清除输入框账号
                    case R.id.iv_account_cancel:
                        etAccount.setText("");
                        break;
                    //清除输入框密码
                    case R.id.iv_password_cancel:
                        etPassword.setText("");
                        break;
                }
            }
        };
        etAccount.setText(UserInfoHelper.isLogin() ? UserInfoHelper.getUserInfo().getMobile() : "");
        tvGetCode.setOnClickListener(onClickListener);
        ivAccountCancel.setOnClickListener(onClickListener);
        ivPasswordCancel.setOnClickListener(onClickListener);
        btnSubmit.setOnClickListener(onClickListener);
        etAccount.addTextChangedListener(accountChangeListener);
        etPassword.addTextChangedListener(passwordChangeListener);
        //监听焦点获悉情况
        etAccount.setOnFocusChangeListener(onFocusChangeListener);
        etPassword.setOnFocusChangeListener(onFocusChangeListener);
        etCode.setOnFocusChangeListener(onFocusChangeListener);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_edit_password;
    }

    @Override
    public void init() {
        initViews();
    }


    /**
     * 获取验证码
     */
    private void cureateGetNumberCode() {
        String account = etAccount.getText().toString().trim();
        if (null != mLoginGroupActivity && !mLoginGroupActivity.isFinishing() && null != mPresenter) {
            mPresenter.getCode(account);
        }
    }


    /**
     * 准备提交新密码
     */
    private void cureateSubmit() {

        String account = etAccount.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        mPresenter.resetPwd(account, code, password);
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
            if (null != ivAccountCancel)
                ivAccountCancel.setVisibility(!TextUtils.isEmpty(s) && s.length() > 0 ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 密码输入框监听
     */
    private TextWatcher passwordChangeListener = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (null != ivPasswordCancel)
                ivPasswordCancel.setVisibility(!TextUtils.isEmpty(s) && s.length() > 0 ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

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
                        if (etAccount.getText().toString().length() > 0) {
                            ivAccountCancel.setVisibility(View.VISIBLE);
                        }
                    } else {
                        ivAccountCancel.setVisibility(View.INVISIBLE);
                    }
                    break;
                case R.id.et_password:
                    if (hasFocus) {
                        if (etPassword.getText().toString().length() > 0) {
                            ivPasswordCancel.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (null != ivPasswordCancel)
                            ivPasswordCancel.setVisibility(View.INVISIBLE);
                    }
                    break;
            }
        }
    };


    //=======================================修改密码结果回调========================================

    @Override
    public void showAccountResult(UserInfo data, String tint) {
        if (null != mLoginGroupActivity && !mLoginGroupActivity.isFinishing()) {
            mLoginGroupActivity.registerResultFinlish();
        }

    }


    @Override
    public void showLoadingDialog(String mess) {
        ((BaseActivity) getActivity()).showLoadingDialog(mess);
    }


    @Override
    public void dismissDialog() {
        ((BaseActivity) getActivity()).dismissDialog();
    }


    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void showErrorAccount() {
        etAccount.startAnimation(mInputAnimation);
    }

    @Override
    public void showErrorPassword() {
        etPassword.startAnimation(mInputAnimation);
    }

    @Override
    public void showErrorCode() {
        etCode.startAnimation(mInputAnimation);
    }

    @Override
    public void showGetCode() {
        mLoginGroupActivity.showGetCodeDisplay(tvGetCode);
    }
}

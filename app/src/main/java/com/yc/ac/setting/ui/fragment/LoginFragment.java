package com.yc.ac.setting.ui.fragment;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.ac.R;
import com.yc.ac.setting.contract.LoginContract;
import com.yc.ac.setting.model.bean.UserInfo;
import com.yc.ac.setting.presenter.LoginPresenter;
import com.yc.ac.setting.ui.activity.LoginGroupActivity;
import com.yc.ac.utils.ToastUtils;
import com.yc.ac.utils.UserInfoHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import yc.com.base.BaseFragment;

/**
 * TinyHung@Outlook.com
 * 2017/11/28.
 * 账号密码登录
 */

public class LoginFragment extends BaseFragment<LoginPresenter> implements LoginContract.View {

    private static final String TAG = "LoginFragment";
    @BindView(R.id.tv_retrieve_password)
    TextView tvRetrievePassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.iv_account_cancel)
    ImageView ivAccountCancel;
    @BindView(R.id.iv_password_cancel)
    ImageView ivPasswordCancel;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_register)
    TextView tvRegister;

    private Animation mInputAnimation;
    private LoginGroupActivity mLoginGroupActivity;
    private List<Fragment> fragmentList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLoginGroupActivity = (LoginGroupActivity) context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void init() {
        fragmentList = new ArrayList<>();

        fragmentList.add(new LoginPhoneFragment());
        fragmentList.add(new LoginCodeFragment());

        initViews();
    }

    /**
     * 叠加界面
     *
     * @param centerTitle 中间标题
     */
    public void addReplaceFragment(int position, String centerTitle) {

        FragmentManager supportFragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();


        for (Fragment fragment : fragmentList) {
            if (fragment.isAdded()) {
                fragmentTransaction.hide(fragment);
            }

        }

        fragmentTransaction.add(R.id.login_container, fragmentList.get(position), "");
        fragmentTransaction.addToBackStack(centerTitle);
        fragmentTransaction.commit();
    }


    protected void initViews() {

        mPresenter = new LoginPresenter(getActivity(), this);

        addReplaceFragment(0, "phone");
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    //登录
                    case R.id.btn_login:
                        createAccountLogin();
                        break;
                    //忘记密码
                    case R.id.tv_retrieve_password:
                        if (null != mLoginGroupActivity && !mLoginGroupActivity.isFinishing()) {
                            mLoginGroupActivity.addReplaceFragment(LoginGroupActivity.FIND_PWD, "修改密码", "登录");//打开修改密码界面
                            mLoginGroupActivity.showOthreLoginView(false);
                        }
                        break;
                    //清除输入框账号
                    case R.id.iv_account_cancel:
                        etAccount.setText("");
                        break;
                    //清除输入框密码
                    case R.id.iv_password_cancel:
                        etPassword.setText("");
                        break;
                    case R.id.tv_register:
                        mLoginGroupActivity.openBtnAction();
                        break;
                }
            }
        };
        etAccount.setText(UserInfoHelper.isLogin() ? UserInfoHelper.getUserInfo().getMobile() : "");
        etAccount.setSelection(etAccount.getText().toString().length());
        tvRetrievePassword.setOnClickListener(onClickListener);
        ivAccountCancel.setOnClickListener(onClickListener);
        ivPasswordCancel.setOnClickListener(onClickListener);
        btnLogin.setOnClickListener(onClickListener);
        tvRegister.setOnClickListener(onClickListener);
        etAccount.addTextChangedListener(accountChangeListener);
        etPassword.addTextChangedListener(passwordChangeListener);
        //监听焦点获悉情况
        etAccount.setOnFocusChangeListener(onFocusChangeListener);
        etPassword.setOnFocusChangeListener(onFocusChangeListener);
        mInputAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        //设置密码属性
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }


    /**
     * 用户使用账号登录
     */
    private void createAccountLogin() {
        if (null != etAccount && null != etPassword) {
            String account = etAccount.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            mPresenter.login(account, password,"");

        }
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
                        if (null != ivAccountCancel) ivAccountCancel.setVisibility(View.INVISIBLE);
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        etAccount.setText("");
        etPassword.setText("");
        if (null != mInputAnimation) {
            mInputAnimation.cancel();
            mInputAnimation = null;
        }
    }




    public void showAccountResult(UserInfo data, String tint) {
        if (null != data && !TextUtils.isEmpty(data.getId())) {
            if (TextUtils.equals(getString(R.string.login), tint)) {
//                APP.getInstance().setUserData(data, true);
            }
            if (null != mLoginGroupActivity && !mLoginGroupActivity.isFinishing()) {
                mLoginGroupActivity.loginResultFinlish();
            }
        } else {
            ToastUtils.showCenterToast(getActivity(), tint + "异常，请重试！");
        }
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

    }

    @Override
    public void showGetCode() {

    }

}

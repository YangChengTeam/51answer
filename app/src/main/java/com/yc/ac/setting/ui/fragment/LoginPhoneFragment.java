package com.yc.ac.setting.ui.fragment;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vondear.rxtools.RxRegTool;
import com.yc.ac.R;
import com.yc.ac.setting.ui.activity.LoginGroupActivity;
import com.yc.ac.utils.ToastUtils;
import com.yc.ac.utils.UserInfoHelper;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import yc.com.base.BaseFragment;

/**
 * Created by wanglin  on 2019/1/5 11:12.
 */
public class LoginPhoneFragment extends BaseFragment {

    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.iv_cancel)
    ImageView ivAccountCancel;
    private LoginGroupActivity mLoginGroupActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLoginGroupActivity = (LoginGroupActivity) context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login_phone;
    }

    @Override
    public void init() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    //下一步
                    case R.id.tv_next:
                        skipFragment();
                        break;
                    case R.id.iv_cancel:
                        etAccount.setText("");
                        etAccount.setHint("请输入手机号");
                        tvNext.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.login_btn_bg_normal));
                        break;
                }
            }
        };
        etAccount.setText(UserInfoHelper.isLogin() ? UserInfoHelper.getUserInfo().getMobile() : "");
        etAccount.setSelection(etAccount.getText().toString().length());
        tvNext.setOnClickListener(onClickListener);
        ivAccountCancel.setOnClickListener(onClickListener);

        etAccount.addTextChangedListener(accountChangeListener);

        //监听焦点获悉情况
        etAccount.setOnFocusChangeListener(onFocusChangeListener);
    }

    private void skipFragment() {
        String phone = etAccount.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || phone.length() != 11) {
            return;
        }
        if (!RxRegTool.isMobileExact(phone)) {
            ToastUtils.showCenterToast(getActivity(), "手机号码不正确");
            return;
        }

//        RxBus.get().post(BusAction.LOGIN_CODE, phone);
        mLoginGroupActivity.addReplaceFragment(LoginGroupActivity.LOGIN_PWD, "登录", "注册", phone);

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
            if (s.toString().trim().length() == 11 && RxRegTool.isMobileExact(s.toString().trim())) {
                tvNext.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.login_btn_bg_select));
            } else {
                tvNext.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.login_btn_bg_normal));
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
                        if (etAccount.getText().toString().length() > 0) {
                            ivAccountCancel.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (null != ivAccountCancel) ivAccountCancel.setVisibility(View.INVISIBLE);
                    }
                    break;
            }
        }
    };


}

package com.yc.answer.setting.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kk.utils.LogUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yc.answer.R;
import com.yc.answer.setting.contract.LoginContract;
import com.yc.answer.setting.model.bean.UserDataInfo;
import com.yc.answer.setting.model.bean.UserInfo;
import com.yc.answer.setting.presenter.LoginPresenter;
import com.yc.answer.setting.ui.fragment.ForgetPwdCodeFragment;
import com.yc.answer.setting.ui.fragment.LoginCodeFragment;
import com.yc.answer.setting.ui.fragment.LoginEditPasswordFragment;
import com.yc.answer.setting.ui.fragment.LoginFragment;
import com.yc.answer.setting.ui.fragment.LoginNewPwdFragment;
import com.yc.answer.setting.ui.fragment.LoginPhoneFragment;
import com.yc.answer.setting.ui.fragment.LoginPwdFragment;
import com.yc.answer.setting.ui.fragment.LoginRegisterFragment;
import com.yc.answer.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import yc.com.base.BaseActivity;

/**
 * TinyHung@Outlook.com
 * 2017/11/28.
 * 用户登录、注册、修改密码
 */

public class LoginGroupActivityNew extends BaseActivity<LoginPresenter> implements LoginContract.View {

    private static final String TAG = "LoginGroupActivity";

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_register)
    TextView btnRegister;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_other_login_tips)
    TextView tvOtherLoginTips;
    @BindView(R.id.re_weichat)
    RelativeLayout reWeichat;
    @BindView(R.id.re_qq)
    RelativeLayout reQq;
    @BindView(R.id.re_weibo)
    RelativeLayout reWeibo;
    @BindView(R.id.ll_other_login_view)
    LinearLayout llOtherLoginView;

    private Map<String, Fragment> fragmentMap;
    public static final String LOGIN = "login";
    public static final String REGISTER = "register";
    public static final String FIND_PWD = "find_pwd";

    public static final String LOGIN_PHONE = "login_phone";

    public static final String LOGIN_CODE = "login_code";

    public static final String LOGIN_PWD = "login_pwd";

    public static final String FORGET_PWD_CODE = "forget_pwd_code";

    public static final String LOGIN_NEW_PWD = "login_new_pwd";

//    public static final String

    @Override
    public int getLayoutId() {
        return R.layout.activity_login_group;
    }

    @Override
    public void init() {
        mPresenter = new LoginPresenter(this, this);
        fragmentMap = new HashMap<>();


        fragmentMap.put(LOGIN_PHONE, new LoginPhoneFragment());
        fragmentMap.put(LOGIN_CODE, new LoginCodeFragment());
        fragmentMap.put(LOGIN_PWD, new LoginPwdFragment());
        fragmentMap.put(FORGET_PWD_CODE, new ForgetPwdCodeFragment());
        fragmentMap.put(LOGIN_NEW_PWD, new LoginNewPwdFragment());

        initViews();
    }

    private void initViews() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_back:
                        onBackPressed();
                        break;
                    case R.id.btn_register:
                        openBtnAction();
                        break;
                    //微信登录
                    case R.id.re_weichat:
                        login(SHARE_MEDIA.WEIXIN);
                        break;
                    //QQ登录
                    case R.id.re_qq:
                        login(SHARE_MEDIA.QQ);
                        break;
                    //微博登录
                    case R.id.re_weibo:
                        login(SHARE_MEDIA.SINA);
                        break;
                }
            }
        };
        btnBack.setOnClickListener(onClickListener);
        btnRegister.setOnClickListener(onClickListener);
        reWeichat.setOnClickListener(onClickListener);
        reQq.setOnClickListener(onClickListener);
        reWeibo.setOnClickListener(onClickListener);
        addReplaceFragment(LOGIN_PHONE, "登录", "注册");//初始化默认登录界面
        tvOtherLoginTips.setText("快捷登录");
        showOthreLoginView(false);
    }

    /**
     * 打开意图
     */
    public void openBtnAction() {
        if (TextUtils.equals("注册", btnRegister.getText().toString())) {
            addReplaceFragment(REGISTER, "注册", "登录");
            tvOtherLoginTips.setText("快速注册");
        } else if (TextUtils.equals("登录", btnRegister.getText().toString())) {
            onBackPressed();
        }
    }

    /**
     * 叠加界面
     *
     * @param flag        片段目标
     * @param centerTitle 中间标题
     * @param rightTitle  右边小标题
     */
    public void addReplaceFragment(String flag, String centerTitle, String rightTitle, String... phone) {
        tvTitle.setText(centerTitle);
        btnRegister.setText(rightTitle);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();

//        for (Map.Entry<String, Fragment> entry : fragmentMap.entrySet()) {
//            Fragment fragment = entry.getValue();
//            if (fragment.isAdded()) {
//                fragmentTransaction.hide(fragment);
//            }
//        }
        Fragment fragment = fragmentMap.get(flag);
        if (fragment instanceof LoginPwdFragment || (fragment instanceof LoginCodeFragment
                || fragment instanceof ForgetPwdCodeFragment || fragment instanceof LoginNewPwdFragment) && (phone != null && phone.length > 0)) {
            Bundle bundle = new Bundle();
            bundle.putString("phone", phone[0]);
            if (phone.length > 1) {
                bundle.putString("code", phone[1]);
            }
            fragment.setArguments(bundle);
        }
        fragmentTransaction.replace(R.id.frame_layout, fragment, centerTitle);
        fragmentTransaction.addToBackStack(centerTitle);
        fragmentTransaction.commit();
    }

    /**
     * 显示和占位第三方登录
     *
     * @param flag
     */
    public void showOthreLoginView(boolean flag) {
        //flag ? View.VISIBLE : View.INVISIBLE
        llOtherLoginView.setVisibility(flag ? View.VISIBLE : View.INVISIBLE);
    }


    //=====================================QQ、微信、微博登录========================================

    /**
     * 拿到用户信息后登录到应用服务器
     *
     * @param userDataInfo
     */
    private void login(UserDataInfo userDataInfo) {
        mPresenter.snsLogin(userDataInfo);
    }


    /**
     * 用户注册\修改密码成功
     */
    public void registerResultFinlish() {
//        onBackPressed();
        loginResultFinlish();
    }

    /**
     * 手机号码、第三方登录成功调用此方法
     */
    public void loginResultFinlish() {
//        UserInfo userData = APP.getInstance().getUserData();
//        if (null != userData && TextUtils.isEmpty(userData.getMobile())) {
//            //如果第三方用户登录成功，可以在这里判断是否需要补全手机号码等信息
//        }
//        RxBus.get().post(Constant.RX_LOGIN_SUCCESS, "login_success");
        onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    /**
     * QQ、微信、微博 登录
     *
     * @param media
     */
    public void login(SHARE_MEDIA media) {
        boolean isauth = UMShareAPI.get(LoginGroupActivityNew.this).isAuthorize(LoginGroupActivityNew.this, media);//判断当前APP有没有授权登录
        if (isauth) {
            UMShareAPI.get(LoginGroupActivityNew.this).getPlatformInfo(LoginGroupActivityNew.this, media, LoginAuthListener);//获取用户信息
        } else {
            UMShareAPI.get(LoginGroupActivityNew.this).doOauthVerify(LoginGroupActivityNew.this, media, LoginAuthListener);//用户授权登录
        }
    }


    /**
     * QQ 微信 微博 登陆后回调
     */
    UMAuthListener LoginAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            showLoadingDialog("登录中，请稍后...");
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            int loginType = 0;
            switch (platform) {
                case QQ:
                    loginType = 1;
                    break;
                case WEIXIN:
                    loginType = 2;
                    break;
                case SINA:
                    loginType = 4;
                    break;
            }
            try {
                if (null != data && data.size() > 0) {
                    LogUtil.msg(TAG + "  data=" + data.toString());
                    UserDataInfo userDataInfo = new UserDataInfo();
                    userDataInfo.setLoginType(loginType + "");
                    //新浪微博
                    if (platform == SHARE_MEDIA.SINA) {
                        userDataInfo.setNickname(data.get("name"));
                        userDataInfo.setCity(data.get("location"));
                        userDataInfo.setIconUrl(data.get("iconurl"));
                        userDataInfo.setGender(data.get("gender"));
                        userDataInfo.setOpenid(data.get("uid"));
                        userDataInfo.setAccessToken(data.get("accessToken"));
                        //微信、QQ
                    } else {
                        userDataInfo.setNickname(data.get("name"));
                        userDataInfo.setCity(data.get("city"));
                        userDataInfo.setIconUrl(data.get("iconurl"));
                        userDataInfo.setGender(data.get("gender"));
                        userDataInfo.setOpenid(data.get("openid"));
                        userDataInfo.setAccessToken(data.get("accessToken"));
                    }
                    //授权成功
                    if (TextUtils.isEmpty(userDataInfo.getNickname()) && TextUtils.isEmpty(userDataInfo.getIconUrl())) {
                        login(platform);
                    } else {
                        //登录App成功,防止微博获取不到用户信息
                        if (!TextUtils.isEmpty(userDataInfo.getOpenid())) {
                            login(userDataInfo);
                        } else {
                            login(platform);
                        }
                    }
                } else {
                    ToastUtils.showCenterToast(LoginGroupActivityNew.this, "登录失败，请重试!");
                }
            } catch (Exception e) {

                ToastUtils.showCenterToast(LoginGroupActivityNew.this, "登录失败，请重试!");
            }
            dismissDialog();
        }


        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            dismissDialog();

            String error = t.getMessage();
            if (!TextUtils.isEmpty(error) && error.contains("2008")) {

                ToastUtils.showCenterToast(LoginGroupActivityNew.this, platform.name() + "没有安装");
            } else {
                ToastUtils.showCenterToast(LoginGroupActivityNew.this, "登录失败，请重试!");
            }

        }

        //
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            dismissDialog();
            ToastUtils.showCenterToast(LoginGroupActivityNew.this, "登录取消");
        }
    };


    //======================================登录到服务器回调=========================================


    public void showAccountResult(UserInfo data, String tint) {

        if (null != data && !TextUtils.isEmpty(data.getId())) {
            if (TextUtils.equals(getString(R.string.login), tint)) {
//                APP.getInstance().setUserData(data, true);
            }
            if (!isFinishing()) {
                loginResultFinlish();
            }
        } else {
            ToastUtils.showCenterToast(LoginGroupActivityNew.this, tint + "异常，请重试！");
        }
    }

    @Override
    public void onBackPressed() {

        LogUtil.msg("TAG :" + getSupportFragmentManager().getBackStackEntryCount());
        //只剩登录一个界面了
        if (getSupportFragmentManager().getBackStackEntryCount() == 1 && !LoginGroupActivityNew.this.isFinishing()) {
            finish();
            return;
        }

        //栈顶存在两个
        if (getSupportFragmentManager().getBackStackEntryCount() >= 2 && !LoginGroupActivityNew.this.isFinishing()) {
            tvTitle.setText("登录");
            btnRegister.setText("注册");
            tvOtherLoginTips.setText("快捷登录");
//            showOthreLoginView(true);
        }
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.menu_exit);//出场动画
    }

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
}

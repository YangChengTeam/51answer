package com.yc.ac.setting.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.ac.R;
import com.yc.ac.constant.BusAction;
import com.yc.ac.setting.contract.LoginContract;
import com.yc.ac.setting.model.bean.TokenInfo;
import com.yc.ac.setting.model.bean.UserDataInfo;
import com.yc.ac.setting.model.bean.UserInfo;
import com.yc.ac.setting.model.engine.LoginGroupEngine;
import com.yc.ac.utils.EngineUtils;
import com.yc.ac.utils.ToastUtils;
import com.yc.ac.utils.UserInfoHelper;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;
import yc.com.base.UIUtils;


/**
 * Created by wanglin  on 2018/3/7 15:10.
 */

public class LoginPresenter extends BasePresenter<LoginGroupEngine, LoginContract.View> implements LoginContract.Presenter {
    public LoginPresenter(Context context, LoginContract.View view) {
        super(context, view);
        mEngine = new LoginGroupEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {

    }


    public void register(String mobile, String password, String code) {
        if (TextUtils.isEmpty(mobile)) {
            mView.showErrorAccount();
            ToastUtils.showCenterToast(mContext, "手机号码不能为空");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mView.showErrorPassword();
            ToastUtils.showCenterToast(mContext, "密码不能为空");
            return;
        }

        if (TextUtils.isEmpty(code)) {
            mView.showErrorCode();
            ToastUtils.showCenterToast(mContext, "验证码不能为空");
            return;
        }
        mView.showLoadingDialog("注册中，请稍候...");
        Subscription subscription = mEngine.register(mobile, code, password).subscribe(new Subscriber<ResultInfo<TokenInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dismissDialog();
            }

            @Override
            public void onNext(ResultInfo<TokenInfo> stringResultInfo) {
                if (stringResultInfo != null) {
                    if (stringResultInfo.code == HttpConfig.STATUS_OK && stringResultInfo.data != null) {
                        UserInfoHelper.setToken(stringResultInfo.data.getToken());
                        getUserInfo(stringResultInfo.data.getToken());
                    } else {
                        mView.dismissDialog();
                        ToastUtils.showCenterToast(mContext, stringResultInfo.message);
                    }
                }
            }
        });

        mSubscriptions.add(subscription);

    }


    private void getUserInfo(String token) {
        Subscription subscription = EngineUtils.getUserInfo(mContext, token).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dismissDialog();
            }

            @Override
            public void onNext(ResultInfo<UserInfo> userInfoResultInfo) {
                mView.dismissDialog();
                if (userInfoResultInfo != null && userInfoResultInfo.code == HttpConfig.STATUS_OK && userInfoResultInfo.data != null) {
                    UserInfoHelper.setUserInfo(userInfoResultInfo.data);
                    RxBus.get().post(BusAction.LOGIN_SUCCESS, userInfoResultInfo.data);
                    mView.finish();
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    public void getCode(String phone) {
        if (TextUtils.isEmpty(phone)) {
            mView.showErrorAccount();
            ToastUtils.showCenterToast(mContext, "手机号码不能为空");
            return;
        }
        if (!UIUtils.isPhoneNumber(phone)) {
            ToastUtils.showCenterToast(mContext, "手机号格式不正确");
            mView.showErrorAccount();
            return;
        }

        mView.showGetCode();
        Subscription subscription = EngineUtils.getCode(mContext, phone).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                if (stringResultInfo != null) {
                    if (stringResultInfo.code == HttpConfig.STATUS_OK && stringResultInfo.data != null) {
                        ToastUtils.showCenterToast(mContext, stringResultInfo.data);
                    } else {
                        ToastUtils.showCenterToast(mContext, stringResultInfo.message);
                    }
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    public void login(String mobile, String password, String code) {

        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showCenterToast(mContext, "手机号不能为空");
            mView.showErrorAccount();
            return;
        }
        if (!UIUtils.isPhoneNumber(mobile)) {
            ToastUtils.showCenterToast(mContext, "手机号格式不正确");
            mView.showErrorAccount();
            return;
        }
        if (TextUtils.isEmpty(password) && TextUtils.isEmpty(code)) {
            ToastUtils.showCenterToast(mContext, "密码或验证码不能为空");
            mView.showErrorPassword();
            return;
        }

        mView.showLoadingDialog("登录中，请稍候...");
        Subscription subscription = mEngine.login(mobile, password, code).subscribe(new Subscriber<ResultInfo<TokenInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dismissDialog();
            }

            @Override
            public void onNext(ResultInfo<TokenInfo> tokenInfoResultInfo) {
                mView.dismissDialog();
                if (tokenInfoResultInfo != null) {
                    if (tokenInfoResultInfo.code == HttpConfig.STATUS_OK && tokenInfoResultInfo.data != null) {
                        UserInfoHelper.setToken(tokenInfoResultInfo.data.getToken());
                        getUserInfo(tokenInfoResultInfo.data.getToken());

                    } else {
                        ToastUtils.showCenterToast(mContext, tokenInfoResultInfo.message);
                    }
                } else {
                    ToastUtils.showCenterToast(mContext, HttpConfig.NET_ERROR);
                }
            }
        });
        mSubscriptions.add(subscription);
    }


    public void resetPwd(String mobile, String code, String new_password) {
        if (TextUtils.isEmpty(mobile)) {
            mView.showErrorAccount();
            ToastUtils.showCenterToast(mContext, "手机号码不能为空");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            mView.showErrorCode();
            ToastUtils.showCenterToast(mContext, "验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(new_password)) {
            mView.showErrorPassword();
            ToastUtils.showCenterToast(mContext, "密码不能为空");
            return;
        }
        mView.showLoadingDialog("重置密码中，请稍候...");
        Subscription subscription = mEngine.resetPassword(mobile, code, new_password).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dismissDialog();
            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                mView.dismissDialog();
                if (stringResultInfo != null) {
                    if (stringResultInfo.code == HttpConfig.STATUS_OK && stringResultInfo.data != null) {
                        ToastUtils.showCenterToast(mContext, stringResultInfo.data);
                        mView.showAccountResult(UserInfoHelper.getUserInfo(), mContext.getString(R.string.reset_password));
                        mView.finish();
                    } else {
                        ToastUtils.showCenterToast(mContext, stringResultInfo.message);
                    }
                }
            }
        });
        mSubscriptions.add(subscription);

    }


    public void snsLogin(UserDataInfo userDataInfo) {

        Subscription subscription = mEngine.snsLogin(userDataInfo.getAccessToken(), userDataInfo.getLoginType(), userDataInfo.getNickname(), userDataInfo.getIconUrl()).subscribe(new Subscriber<ResultInfo<TokenInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<TokenInfo> tokenInfoResultInfo) {
                if (tokenInfoResultInfo != null && tokenInfoResultInfo.code == HttpConfig.STATUS_OK && tokenInfoResultInfo.data != null) {
                    UserInfoHelper.setToken(tokenInfoResultInfo.data.getToken());
                    getUserInfo(tokenInfoResultInfo.data.getToken());
                }
            }
        });
        mSubscriptions.add(subscription);
    }

}

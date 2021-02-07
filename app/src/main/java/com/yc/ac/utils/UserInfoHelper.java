package com.yc.ac.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.RxTool;
import com.yc.ac.R;
import com.yc.ac.constant.HttpStatus;
import com.yc.ac.constant.SpConstant;
import com.yc.ac.setting.model.bean.UserInfo;
import com.yc.ac.setting.ui.activity.LoginGroupActivity;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import yc.com.base.EmptyUtils;
import yc.com.base.UIUtils;

/**
 * Created by wanglin  on 2018/3/9 19:17.
 */

public class UserInfoHelper {

    private static UserInfo mUserInfo;

    public static UserInfo getUserInfo() {
        if (mUserInfo != null) {
            return mUserInfo;
        }

        String userinfoStr = RxSPTool.getString(RxTool.getContext(), SpConstant.USER_INFO);
        UserInfo userInfo = null;
        try {
            userInfo = JSON.parseObject(userinfoStr, UserInfo.class);
        } catch (Exception e) {
            RxLogTool.e("error->" + e);
        }
        mUserInfo = userInfo;
        return mUserInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        try {
            String userinfoStr = JSON.toJSONString(userInfo);
            RxSPTool.putString(RxTool.getContext(), SpConstant.USER_INFO, userinfoStr);
            mUserInfo = userInfo;
        } catch (Exception e) {
            RxLogTool.e("error->" + e);
        }
    }

    public static String getUId() {
        if (isLogin()) {
            return getUserInfo().getId();
        }
        return "";
    }

    private static String mToken;

    /**
     * 保存token信息
     *
     * @param token
     */
    public static void setToken(String token) {

        try {
            RxSPTool.putString(RxTool.getContext(), SpConstant.TOKEN, token);
            mToken = token;
        } catch (Exception e) {
            RxLogTool.e("error->" + e);
        }
    }

    public static String getToken() {
        if (!TextUtils.isEmpty(mToken)) {
            return mToken;
        }
        try {
            mToken = RxSPTool.getString(RxTool.getContext(), SpConstant.TOKEN);
        } catch (Exception e) {
            RxLogTool.e("error->" + e);
        }
        return mToken;
    }


    public static boolean isGoToLogin(Activity activity) {
        if (!isLogin()) {
            activity.startActivity(new Intent(activity, LoginGroupActivity.class));
            activity.overridePendingTransition(R.anim.menu_enter, 0);
            return true;
        }
        return false;

    }

    public interface Callback {
        void showUserInfo(UserInfo userInfo);

        void showNoLogin();
    }

    public static void getUserInfoDo(final Callback callback) {
        Observable.just("").map(s -> UserInfoHelper.getUserInfo()).subscribeOn(Schedulers.io()).filter(userInfo -> {
            boolean flag = !EmptyUtils.isEmpty(userInfo);
            if (!flag) {
                UIUtils.post(callback::showNoLogin);
            }
            return flag;
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<UserInfo>() {
            @Override
            public void call(UserInfo userInfo) {
                callback.showUserInfo(userInfo);
            }
        });
    }


    public static void login(final Context context) {
        if (TextUtils.isEmpty(getToken())) return;
        EngineUtils.getUserInfo(context, getToken()).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                UIUtils.postDelay(new Runnable() {
                    @Override
                    public void run() {
                        login(context);
                    }
                }, 500);
            }

            @Override
            public void onNext(ResultInfo<UserInfo> userInfoResultInfo) {
                if (userInfoResultInfo != null) {
                    if (userInfoResultInfo.getCode() == HttpConfig.STATUS_OK && userInfoResultInfo.getData() != null) {
                        setUserInfo(userInfoResultInfo.getData());
                    } else if (userInfoResultInfo.getCode() == HttpStatus.TOKEN_EXPIRED) {
                        setUserInfo(null);
                        setToken("");
                    }

                }
            }
        });
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    public static boolean isLogin() {
        return getUserInfo() != null;
    }




}

package com.yc.ac.setting.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.ac.constant.BusAction;
import com.yc.ac.setting.contract.BindPhoneContract;
import com.yc.ac.setting.model.bean.UserInfo;
import com.yc.ac.setting.model.engine.BindPhoneEngine;
import com.yc.ac.utils.EngineUtils;
import com.yc.ac.utils.ToastUtils;
import com.yc.ac.utils.UserInfoHelper;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;
import yc.com.base.UIUtils;

/**
 * Created by wanglin  on 2018/3/15 10:37.
 */

public class BindPhonePresenter extends BasePresenter<BindPhoneEngine, BindPhoneContract.View> implements BindPhoneContract.Presenter {
    public BindPhonePresenter(Context context, BindPhoneContract.View view) {
        super(context, view);
        mEngine = new BindPhoneEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {

    }


    @Override
    public void changePhone(String mobile, String code) {
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showCenterToast(mContext, "手机号不能为空");
            mView.showErrorAccount();
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showCenterToast(mContext, "验证码不能为空");
            mView.showErrorCode();
            return;
        }
        mView.showLoadingDialog("");
        Subscription subscription = mEngine.changePhone(mobile, code).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
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
                if (userInfoResultInfo != null) {
                    if (userInfoResultInfo.getCode() == HttpConfig.STATUS_OK && userInfoResultInfo.getData() != null) {
                        UserInfoHelper.saveUserInfo(userInfoResultInfo.getData());
                        RxBus.get().post(BusAction.LOGIN_SUCCESS, userInfoResultInfo.getData());
                        mView.finish();
                    } else {
                        ToastUtils.showCenterToast(mContext, userInfoResultInfo.getMsg());
                    }
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void bindPhone(String mobile, String code) {
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showCenterToast(mContext, "手机号不能为空");
            mView.showErrorAccount();
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showCenterToast(mContext, "验证码不能为空");
            mView.showErrorCode();
            return;
        }
        mView.showLoadingDialog("");
        Subscription subscription = mEngine.bindPhone(mobile, code).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
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
                if (userInfoResultInfo != null) {
                    if (userInfoResultInfo.getCode() == HttpConfig.STATUS_OK && userInfoResultInfo.getData() != null) {
                        UserInfoHelper.saveUserInfo(userInfoResultInfo.getData());
                        RxBus.get().post(BusAction.LOGIN_SUCCESS, userInfoResultInfo.getData());
                        mView.finish();
                    } else {
                        ToastUtils.showCenterToast(mContext, userInfoResultInfo.getMsg());
                    }
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
                    if (stringResultInfo.getCode() == HttpConfig.STATUS_OK && stringResultInfo.getData() != null) {
                        ToastUtils.showCenterToast(mContext, stringResultInfo.getData());
                    } else {
                        ToastUtils.showCenterToast(mContext, stringResultInfo.getMsg());
                    }
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}

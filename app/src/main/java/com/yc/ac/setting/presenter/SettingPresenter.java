package com.yc.ac.setting.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.ac.constant.BusAction;
import com.yc.ac.setting.contract.SettingContract;
import com.yc.ac.setting.model.bean.UploadInfo;
import com.yc.ac.setting.model.bean.UserInfo;
import com.yc.ac.setting.model.engine.SettingEngine;
import com.yc.ac.utils.EngineUtils;
import com.yc.ac.utils.GlideCacheHelper;
import com.yc.ac.utils.ToastUtils;
import com.yc.ac.utils.UserInfoHelper;

import java.io.File;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin  on 2018/3/12 19:14.
 */

public class SettingPresenter extends BasePresenter<SettingEngine, SettingContract.View> implements SettingContract.Presenter {
    public SettingPresenter(Context context, SettingContract.View view) {
        super(context, view);
        mEngine = new SettingEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {
        if (!isForceUI) return;
        getCacheSize();
    }

    public void logout() {
        UserInfoHelper.saveUserInfo(null);
        UserInfoHelper.setToken("");
        RxBus.get().post(BusAction.LOGIN_OUT, "logout");
        ToastUtils.showCenterToast(mContext, "退出登录");
        mView.showLogout();
    }

    private void getCacheSize() {
        mView.showCacheSize(GlideCacheHelper.getInstance(mContext).getCacheSize());
    }

    public boolean clearCache() {
        return GlideCacheHelper.getInstance(mContext).clearCache();
    }


    public void uploadFile(File file, String fileName) {
        mView.showLoadingDialog("正在上传，请稍候...");
        Subscription subscription = EngineUtils.uploadInfo(mContext, file, fileName).subscribe(new Subscriber<ResultInfo<UploadInfo>>() {
            @Override
            public void onCompleted() {
                mView.dismissDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissDialog();
            }

            @Override
            public void onNext(final ResultInfo<UploadInfo> uploadInfoResultInfo) {
                if (uploadInfoResultInfo != null && uploadInfoResultInfo.getCode() == HttpConfig.STATUS_OK && uploadInfoResultInfo.getData() != null) {
                    updateInfo("", uploadInfoResultInfo.getData().url, "");
                }
            }
        });
        mSubscriptions.add(subscription);

    }


    private void updateInfo(String nick_name, String face, String password) {
        if (TextUtils.isEmpty(face)) {
            mView.showLoadingDialog("正在修改信息，请稍候...");
        }
        Subscription subscription = EngineUtils.updateInfo(mContext, nick_name, face, password).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
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
                if (userInfoResultInfo != null && userInfoResultInfo.getCode() == HttpConfig.STATUS_OK && userInfoResultInfo.getData() != null) {
                    UserInfoHelper.saveUserInfo(userInfoResultInfo.getData());
                    RxBus.get().post(BusAction.LOGIN_SUCCESS, userInfoResultInfo.getData());
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    public void userCancellation() {
        Subscription subscription = mEngine.userCancellation().subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                if (stringResultInfo != null) {
                    if (stringResultInfo.getCode() == HttpConfig.STATUS_OK) {
                        mView.cancellationSuccess();
                    } else {
                        ToastUtils.showCenterToast(mContext, stringResultInfo.getMsg());
                    }

                }
            }
        });
        mSubscriptions.add(subscription);
    }
}

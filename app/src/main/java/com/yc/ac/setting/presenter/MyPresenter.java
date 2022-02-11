package com.yc.ac.setting.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.vondear.rxtools.RxSPTool;
import com.yc.ac.base.BaseEngine;
import com.yc.ac.constant.BusAction;
import com.yc.ac.constant.SpConstant;
import com.yc.ac.setting.contract.MyContract;
import com.yc.ac.setting.model.bean.QbInfoWrapper;
import com.yc.ac.setting.model.bean.ShareInfo;
import com.yc.ac.setting.model.bean.TaskLisInfoWrapper;
import com.yc.ac.setting.model.bean.TaskListInfo;
import com.yc.ac.setting.model.bean.UploadInfo;
import com.yc.ac.setting.model.bean.UserInfo;
import com.yc.ac.utils.EngineUtils;
import com.yc.ac.utils.ShareInfoHelper;
import com.yc.ac.utils.ToastUtils;
import com.yc.ac.utils.UserInfoHelper;

import java.io.File;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;
import yc.com.base.CommonInfoHelper;

/**
 * Created by wanglin  on 2018/3/12 17:14.
 */

public class MyPresenter extends BasePresenter<BaseEngine, MyContract.View> implements MyContract.Presenter {
    public MyPresenter(Context context, MyContract.View view) {
        super(context, view);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {
        if (!isForceUI) return;

        getUserInfo();
        getShareInfo();
    }

    @Override
    public void getUserInfo() {

        UserInfoHelper.getUserInfoDo(new UserInfoHelper.Callback() {
            @Override
            public void showUserInfo(UserInfo userInfo) {
                mView.showUserInfo(userInfo);
            }

            @Override
            public void showNoLogin() {
                mView.showNotLogin("");
            }
        });
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


    public void updateInfo(String nick_name, String face, String password) {
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


    public void getShareInfo() {
        Subscription subscription = EngineUtils.getShareInfo(mContext).subscribe(new Subscriber<ResultInfo<ShareInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<ShareInfo> shareInfoResultInfo) {
                if (shareInfoResultInfo != null && shareInfoResultInfo.getCode() == HttpConfig.STATUS_OK && shareInfoResultInfo.getData() != null) {
                    ShareInfoHelper.setShareInfo(shareInfoResultInfo.getData());
                }
            }
        });
        mSubscriptions.add(subscription);
    }


}

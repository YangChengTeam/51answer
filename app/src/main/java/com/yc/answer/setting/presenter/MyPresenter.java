package com.yc.answer.setting.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.ToastUtil;
import com.vondear.rxtools.RxSPTool;
import com.yc.answer.base.BaseEngine;
import com.yc.answer.constant.BusAction;
import com.yc.answer.constant.SpConstant;
import com.yc.answer.setting.contract.MyContract;
import com.yc.answer.setting.model.bean.QbInfoWrapper;
import com.yc.answer.setting.model.bean.ShareInfo;
import com.yc.answer.setting.model.bean.TaskLisInfoWrapper;
import com.yc.answer.setting.model.bean.UploadInfo;
import com.yc.answer.setting.model.bean.UserInfo;
import com.yc.answer.utils.EngineUtils;
import com.yc.answer.utils.ShareInfoHelper;
import com.yc.answer.utils.ToastUtils;
import com.yc.answer.utils.UserInfoHelper;

import java.io.File;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;
import yc.com.base.UIUtils;

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
        getTaskInfoList(false);
        getUserInfo();
        getShareInfo();
        getQbInfo();
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
                if (uploadInfoResultInfo != null && uploadInfoResultInfo.code == HttpConfig.STATUS_OK && uploadInfoResultInfo.data != null) {
                    updateInfo("", uploadInfoResultInfo.data.url, "");
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
                if (userInfoResultInfo != null && userInfoResultInfo.code == HttpConfig.STATUS_OK && userInfoResultInfo.data != null) {
                    UserInfoHelper.setUserInfo(userInfoResultInfo.data);
                    RxBus.get().post(BusAction.LOGIN_SUCCESS, userInfoResultInfo.data);
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
                if (shareInfoResultInfo != null && shareInfoResultInfo.code == HttpConfig.STATUS_OK && shareInfoResultInfo.data != null) {
                    ShareInfoHelper.setShareInfo(shareInfoResultInfo.data);
                }
            }
        });
        mSubscriptions.add(subscription);
    }


    public void comment(String userid) {
        Subscription subscription = EngineUtils.comment(mContext, userid).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<String> stringResultInfo) {
                if (stringResultInfo != null) {
                    if (stringResultInfo.code == HttpConfig.STATUS_OK) {
                        RxSPTool.putBoolean(mContext, SpConstant.OPEN_MARKET, true);
                        getQbInfo();
                    }

//                    else {
//                        UIUtils.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                ToastUtils.showCenterToast(mContext, stringResultInfo.message);
//                            }
//                        });
//
//                    }
                }
            }
        });
        mSubscriptions.add(subscription);
    }


    public void getQbInfo() {
        Subscription subscription = EngineUtils.getQbInfo(mContext).subscribe(new Subscriber<ResultInfo<QbInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<QbInfoWrapper> qbInfoWrapperResultInfo) {
                if (qbInfoWrapperResultInfo != null && qbInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && qbInfoWrapperResultInfo.data != null) {
                    mView.showQbInfo(qbInfoWrapperResultInfo.data.getInfo());
                }
            }
        });

        mSubscriptions.add(subscription);
    }


    public void getTaskInfoList(final boolean isReload) {

        Subscription subscription = EngineUtils.getTaskInfoList(mContext).subscribe(new Subscriber<ResultInfo<TaskLisInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
//                if (!isReload)
//                    mView.showNoNet();
            }

            @Override
            public void onNext(ResultInfo<TaskLisInfoWrapper> taskLisInfoWrapperResultInfo) {
                if (taskLisInfoWrapperResultInfo != null) {

                    if (taskLisInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && taskLisInfoWrapperResultInfo.data != null) {
//                        mView.hide();
                        mView.showTaskList(taskLisInfoWrapperResultInfo.data.getList());
                    } else {
//                        if (!isReload)
//                            mView.showNoData();
                        ToastUtils.showCenterToast(mContext, taskLisInfoWrapperResultInfo.message);
                    }
                } else {
//                    if (!isReload)
//                        mView.showNoNet();
                    ToastUtils.showCenterToast(mContext, HttpConfig.NET_ERROR);
                }
            }
        });

        mSubscriptions.add(subscription);
    }
}

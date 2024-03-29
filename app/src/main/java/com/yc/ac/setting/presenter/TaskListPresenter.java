package com.yc.ac.setting.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.LogUtil;
import com.kk.utils.ToastUtil;
import com.vondear.rxtools.RxSPTool;
import com.yc.ac.constant.SpConstant;
import com.yc.ac.setting.contract.TaskListContract;
import com.yc.ac.setting.model.bean.TaskLisInfoWrapper;
import com.yc.ac.setting.model.engine.TaskListEngine;
import com.yc.ac.utils.EngineUtils;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;
import yc.com.base.UIUtils;

/**
 * Created by wanglin  on 2018/9/13 17:41.
 */
public class TaskListPresenter extends BasePresenter<TaskListEngine, TaskListContract.View> implements TaskListContract.Presenter {


    public TaskListPresenter(Context context, TaskListContract.View view) {
        super(context, view);
        mEngine = new TaskListEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {
        if (!isForceUI) return;
        getTaskInfoList(false);
    }


    @Override
    public void getTaskInfoList(final boolean isReload) {
        if (!isReload)
            mView.showLoading();
        Subscription subscription = mEngine.getTaskInfoList().subscribe(new Subscriber<ResultInfo<TaskLisInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isReload)
                    mView.showNoNet();
            }

            @Override
            public void onNext(ResultInfo<TaskLisInfoWrapper> taskLisInfoWrapperResultInfo) {
                if (taskLisInfoWrapperResultInfo != null) {

                    if (taskLisInfoWrapperResultInfo.getCode() == HttpConfig.STATUS_OK && taskLisInfoWrapperResultInfo.getData() != null) {
                        mView.hide();
                        mView.showTaskList(taskLisInfoWrapperResultInfo.getData().getList());
                    } else {
                        if (!isReload)
                            mView.showNoData();
                    }
                } else {
                    if (!isReload)
                        mView.showNoNet();
                }
            }
        });

        mSubscriptions.add(subscription);
    }


    public void comment() {
        Subscription subscription = EngineUtils.comment(mContext).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<String> stringResultInfo) {
                if (stringResultInfo != null) {
                    if (stringResultInfo.getCode() == HttpConfig.STATUS_OK) {
                        RxSPTool.putBoolean(mContext, SpConstant.OPEN_MARKET, true);
                        getTaskInfoList(true);
                    } else {
                        UIUtils.post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.toast2(mContext, stringResultInfo.getMsg());
                            }
                        });

                    }
                }


            }
        });

        mSubscriptions.add(subscription);
    }


}

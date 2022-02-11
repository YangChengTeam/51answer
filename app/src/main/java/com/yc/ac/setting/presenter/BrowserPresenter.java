package com.yc.ac.setting.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yc.ac.setting.contract.BroswerContract;
import com.yc.ac.setting.model.bean.BrowserInfo;
import com.yc.ac.setting.model.engine.BrowserEngine;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin  on 2019/3/15 10:43.
 */
public class BrowserPresenter extends BasePresenter<BrowserEngine, BroswerContract.View> implements BroswerContract.Presenter {
    private List<BrowserInfo> browserInfoList;

    public BrowserPresenter(Context context, BroswerContract.View view) {
        super(context, view);
        mEngine = new BrowserEngine(context);
        browserInfoList = new ArrayList<>();
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {

    }

    @Override
    public void getBrowserInfos(final int page, int pageSize) {
        if (page == 1) mView.showLoading();
        Subscription subscription = mEngine.getBrowserInfos(page, pageSize).subscribe(new Subscriber<List<BrowserInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (page == 1) mView.showNoNet();
            }

            @Override
            public void onNext(List<BrowserInfo> browserInfos) {
                if (browserInfos != null && browserInfos.size() > 0) {
                    mView.hide();
                    if (page == 1) {
                        browserInfoList.clear();
                    }
                    browserInfoList.addAll(browserInfos);
                    createNewData(browserInfoList);
                } else {
                    if (page == 1)
                        mView.showNoData();
                    else {
                        mView.showEnd();
                    }
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    private void createNewData(List<BrowserInfo> browserInfos) {
        if (browserInfos.size() > 0) {
            recycleData(browserInfos, 0);
        }
    }

    private void recycleData(List<BrowserInfo> browserInfos, int temp) {
        Observable.just(temp).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(t -> {
            BrowserInfo firstInfo = browserInfos.get(t);
//            Log.e("TAG", "recycleData: " + t);
            if (t == browserInfos.size() - 1) {
                mView.showBrowserInfos(browserInfos);
                return;
            }
            int i = t + 1;
            boolean isBreak = false;
            while (i < browserInfos.size() && !isBreak) {
                BrowserInfo browserInfo = browserInfos.get(i);
                if (TextUtils.equals(firstInfo.getBrowserTime(), browserInfo.getBrowserTime())) {
                    browserInfo.setShow(false);
                    if (i == browserInfos.size() - 1) {
                        mView.showBrowserInfos(browserInfos);
                    }
                } else {
                    t = i;
                    isBreak = true;
                }
                if (isBreak) {
                    recycleData(browserInfos, t);
                }
                i++;
            }

        });

    }


    public void deleteBrowserInfo(BrowserInfo browserInfo) {
        if (browserInfo == null) return;

        Subscription subscription = mEngine.deleteBrowserInfos(browserInfo).subscribe(aVoid -> {

        });
        mSubscriptions.add(subscription);

    }

}

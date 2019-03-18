package com.yc.ac.setting.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.yc.ac.setting.contract.BroswerContract;
import com.yc.ac.setting.model.bean.BrowserInfo;
import com.yc.ac.setting.model.engine.BrowserEngine;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin  on 2019/3/15 10:43.
 */
public class BrowserPresenter extends BasePresenter<BrowserEngine, BroswerContract.View> implements BroswerContract.Presenter {
    public BrowserPresenter(Context context, BroswerContract.View view) {
        super(context, view);
        mEngine = new BrowserEngine(context);
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
                    createNewData(browserInfos);
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
        BrowserInfo firstInfo = browserInfos.get(temp);
        for (int i = temp + 1; i < browserInfos.size(); i++) {
            BrowserInfo browserInfo = browserInfos.get(i);
            if (TextUtils.equals(firstInfo.getBrowserTime(), browserInfo.getBrowserTime())) {
                browserInfo.setShow(false);
            } else {
                temp = i;
                List<BrowserInfo> browserInfos1 = browserInfos.subList(i, browserInfos.size());
                recycleData(browserInfos1, temp);
            }
        }
        mView.showBrowserInfos(browserInfos);
    }


    public void deleteBrowserInfo(BrowserInfo browserInfo) {
        if (browserInfo == null) return;

        Subscription subscription = mEngine.deleteBrowserInfos(browserInfo).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        });
        mSubscriptions.add(subscription);

    }

}

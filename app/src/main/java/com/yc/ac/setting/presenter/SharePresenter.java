package com.yc.ac.setting.presenter;

import android.content.Context;

import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.ac.base.MyApp;
import com.yc.ac.constant.BusAction;
import com.yc.ac.setting.contract.ShareContract;
import com.yc.ac.setting.model.bean.ShareInfo;
import com.yc.ac.setting.model.bean.ShareInfoDao;
import com.yc.ac.setting.model.engine.ShareEngine;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin  on 2018/3/22 08:59.
 */

public class SharePresenter extends BasePresenter<ShareEngine, ShareContract.View> implements ShareContract.Presenter {

    private ShareInfoDao shareInfoDao;

    public SharePresenter(Context context, ShareContract.View view) {
        super(context, view);
        mEngine = new ShareEngine(context);
        shareInfoDao = MyApp.getDaoSession().getShareInfoDao();
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {
        if (!isForceUI) return;
//        getShareInfo();

    }


    public void share(final ShareInfo shareInfo) {
        Subscription subscription = mEngine.share(shareInfo.getBook_id()).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                if (stringResultInfo != null && stringResultInfo.getCode() == HttpConfig.STATUS_OK) {
                    RxBus.get().post(BusAction.SHARE_SUCCESS, "share_success");
                } else {
                    saveShare(shareInfo);
                    RxBus.get().post(BusAction.SHARE_SUCCESS, "share_success");
                }
                mView.showSuccess();
            }
        });
        mSubscriptions.add(subscription);
    }


    public void shareMoney() {
        Subscription subscription = mEngine.shareMoney().subscribe(new Subscriber<ResultInfo<String>>() {
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
                        RxBus.get().post(BusAction.SHARE_MONEY_SUCCESS, "success");
                    }
                }

            }
        });
        mSubscriptions.add(subscription);
    }

    private boolean queryShareBook(ShareInfo shareInfo) {

        ShareInfo result = shareInfoDao.queryBuilder().where(ShareInfoDao.Properties.Book_id.eq(shareInfo.getBook_id())).build().unique();
        return result != null;

    }


    private void saveShare(ShareInfo shareInfo) {

        if (!queryShareBook(shareInfo)) {
            shareInfoDao.insert(shareInfo);
        }
    }


}

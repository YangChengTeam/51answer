package com.yc.answer.setting.presenter;

import android.content.Context;

import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.answer.constant.BusAction;
import com.yc.answer.setting.contract.ShareContract;
import com.yc.answer.setting.model.bean.ShareInfo;
import com.yc.answer.setting.model.engine.ShareEngine;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin  on 2018/3/22 08:59.
 */

public class SharePresenter extends BasePresenter<ShareEngine, ShareContract.View> implements ShareContract.Presenter {
    public SharePresenter(Context context, ShareContract.View view) {
        super(context, view);
        mEngine = new ShareEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {
        if (!isForceUI) return;
//        getShareInfo();

    }


    public void share(String book_id) {
        Subscription subscription = mEngine.share(book_id).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                if (stringResultInfo != null && stringResultInfo.code == HttpConfig.STATUS_OK) {
                    RxBus.get().post(BusAction.SHARE_SUCCESS, "share_success");
                    mView.showSuccess();
                }
            }
        });
        mSubscriptions.add(subscription);
    }


}

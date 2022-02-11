package com.yc.ac.index.presenter;

import android.content.Context;

import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.ac.constant.BusAction;
import com.yc.ac.index.contract.PerfectBookContract;
import com.yc.ac.index.model.bean.BookInfo;
import com.yc.ac.index.model.engine.PerfectBookEngine;
import com.yc.ac.utils.ToastUtils;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin  on 2018/4/24 15:28.
 */

public class PerfectBookPresenter extends BasePresenter<PerfectBookEngine, PerfectBookContract.View> implements PerfectBookContract.Presenter {
    public PerfectBookPresenter(Context context, PerfectBookContract.View view) {
        super(context, view);
        mEngine = new PerfectBookEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {

    }

    public void perfectBook(BookInfo bookInfo) {
        mView.showLoadingDialog("正在上传书本答案，请稍候...");
        Subscription subscription = mEngine.perfectBookInfo(bookInfo).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dismissDialog();
            }

            @Override
            public void onNext(ResultInfo<String> bookInfoResultInfo) {
                mView.dismissDialog();
                if (bookInfoResultInfo != null && bookInfoResultInfo.getCode() == HttpConfig.STATUS_OK) {
                    ToastUtils.showCenterToast(mContext, bookInfoResultInfo.getMsg());
                    RxBus.get().post(BusAction.FINISH,"finish");
                    mView.finish();
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}

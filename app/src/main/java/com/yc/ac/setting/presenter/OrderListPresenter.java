package com.yc.ac.setting.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.LogUtil;
import com.yc.ac.setting.contract.OrderListContract;
import com.yc.ac.setting.model.bean.OrderInfo;
import com.yc.ac.setting.model.engine.OrderListEngine;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin on 2021/5/20 9:06
 */
public class OrderListPresenter extends BasePresenter<OrderListEngine, OrderListContract.View> implements OrderListContract.Presenter {
    public OrderListPresenter(Context context, OrderListContract.View view) {
        super(context, view);
        mEngine = new OrderListEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {
        if (!isForceUI) return;
        getOrderInfoList(false);

    }

    @Override
    public void getOrderInfoList(boolean b) {
        if (!b)
            mView.showLoading();
        Subscription subscription = mEngine.getOrderInfoList().subscribe(new Subscriber<ResultInfo<List<OrderInfo>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.msg("error::" + e.getMessage());
                mView.showNoNet();
            }

            @Override
            public void onNext(ResultInfo<List<OrderInfo>> listResultInfo) {
                if (listResultInfo != null) {
                    if (listResultInfo.getCode() == HttpConfig.STATUS_OK && listResultInfo.getData() != null && listResultInfo.getData().size() > 0) {
                        mView.showOrderInfoList(listResultInfo.getData());
                        mView.hide();
                    } else {
                        mView.showNoData();
                    }
                } else {
                    mView.showNoNet();
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}

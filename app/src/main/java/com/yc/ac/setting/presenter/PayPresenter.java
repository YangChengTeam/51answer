package com.yc.ac.setting.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.ac.index.model.bean.PayItemInfo;
import com.yc.ac.pay.PayInfo;
import com.yc.ac.setting.contract.PayContract;
import com.yc.ac.setting.model.engine.PayEngine;
import com.yc.ac.setting.model.engine.WxPayInfo;
import com.yc.ac.utils.ToastUtils;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin on 2021/5/19 17:25
 */
public class PayPresenter extends BasePresenter<PayEngine, PayContract.View> implements PayContract.Presenter {
    public PayPresenter(Context context, PayContract.View view) {
        super(context, view);
        mEngine = new PayEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {
        if (!isForceUI) return;
        getPayItemInfoList();
    }

    @Override
    public void getPayItemInfoList() {
        mView.showLoadingDialog("正在加载中...");
        Subscription subscription = mEngine.getPayItemInfoList().subscribe(new Subscriber<ResultInfo<List<PayItemInfo>>>() {
            @Override
            public void onCompleted() {
                mView.dismissDialog();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<List<PayItemInfo>> listResultInfo) {
                if (listResultInfo != null) {
                    if (listResultInfo.getCode() == HttpConfig.STATUS_OK && listResultInfo.getData() != null) {
                        mView.showPayItemInfos(listResultInfo.getData());
                    } else {
                        ToastUtils.showCenterToast(mContext, listResultInfo.getMsg());
                    }
                }
            }


        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void aliPay(String userId, String goodsId, String classId, String articleId) {
        mView.showLoadingDialog("支付中...");
        Subscription subscription = mEngine.aliPay(userId, goodsId, classId, articleId).subscribe(new Subscriber<ResultInfo<PayInfo>>() {
            @Override
            public void onCompleted() {
                mView.dismissDialog();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<PayInfo> listResultInfo) {
                if (listResultInfo != null) {
                    if (listResultInfo.getCode() == HttpConfig.STATUS_OK && listResultInfo.getData() != null) {
                        mView.showAliPayInfo(listResultInfo.getData());
                    } else {
                        ToastUtils.showCenterToast(mContext, listResultInfo.getMsg());
                    }
                }
            }


        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void wxPay(String userId, String goodsId, String classId, String articleId) {
        mView.showLoadingDialog("支付中...");
        Subscription subscription = mEngine.wxPay(userId, goodsId, classId, articleId).subscribe(new Subscriber<ResultInfo<WxPayInfo>>() {
            @Override
            public void onCompleted() {
                mView.dismissDialog();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<WxPayInfo> wxPayInfoResultInfo) {
                if (wxPayInfoResultInfo != null) {
                    if (wxPayInfoResultInfo.getCode() == HttpConfig.STATUS_OK && wxPayInfoResultInfo.getData() != null) {
                        mView.showWxPayInfo(wxPayInfoResultInfo.getData());
                    } else {
                        ToastUtils.showCenterToast(mContext, wxPayInfoResultInfo.getMsg());
                    }
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}

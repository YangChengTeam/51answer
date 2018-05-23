package com.yc.answer.collect.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.answer.collect.contract.CollectContract;
import com.yc.answer.collect.model.engine.CollectEngine;
import com.yc.answer.constant.HttpStatus;
import com.yc.answer.index.model.bean.BookInfoWrapper;
import com.yc.answer.utils.ToastUtils;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin  on 2018/3/8 18:45.
 */

public class CollectPresenter extends BasePresenter<CollectEngine, CollectContract.View> implements CollectContract.Presenter {


    public CollectPresenter(Context context, CollectContract.View view) {
        super(context, view);
        mEngine = new CollectEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {

    }

    public void getCollectList(final int page, int limit, boolean isRefresh) {
        if (page == 1 && !isRefresh) {
            mView.showLoading();
        }
        Subscription subscription = mEngine.getCollectList(page, limit).subscribe(new Subscriber<ResultInfo<BookInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(ResultInfo<BookInfoWrapper> bookInfoWrapperResultInfo) {
                if (bookInfoWrapperResultInfo != null) {
                    if (bookInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && bookInfoWrapperResultInfo.data != null && bookInfoWrapperResultInfo.data.getLists() != null) {
                        mView.hide();
                        mView.showCollectList(bookInfoWrapperResultInfo.data);
                    } else {
                        if (bookInfoWrapperResultInfo.code == HttpStatus.TOKEN_EXPIRED) {
//                            ToastUtils.showCenterToast(mContext, "请先登录");
                            mView.showTintInfo("查看已收藏书籍，需要先登录");
                            return;
                        }
                        if (page == 1) {
                            mView.showNoData();
                        } else {
                            mView.showEnd();
                        }

                    }
                } else {
                    if (page == 1) mView.showNoNet();
                }
            }
        });

        mSubscriptions.add(subscription);

    }
}

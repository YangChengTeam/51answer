package com.yc.ac.index.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.ac.index.contract.UploadBookListContract;
import com.yc.ac.index.model.bean.BookInfo;
import com.yc.ac.index.model.bean.BookInfoWrapper;
import com.yc.ac.index.model.engine.UploadBookListEngine;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin  on 2018/4/24 16:05.
 */

public class UploadBookListPresenter extends BasePresenter<UploadBookListEngine, UploadBookListContract.View> implements UploadBookListContract.Presenter {
    public UploadBookListPresenter(Context context, UploadBookListContract.View view) {
        super(context, view);
        mEngine = new UploadBookListEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {
//        if (!isForceUI) return;
//        getUploadBookList();
    }

    public void getUploadBookList(final int page, int limit) {
        if (page == 1)
            mView.showLoading();
        Subscription subscription = mEngine.getUploadBookList(page, limit).subscribe(new Subscriber<ResultInfo<BookInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (page == 1)
                    mView.showNoNet();
            }

            @Override
            public void onNext(ResultInfo<BookInfoWrapper> listResultInfo) {
                if (listResultInfo != null) {
                    if (listResultInfo.getCode() == HttpConfig.STATUS_OK && listResultInfo.getData() != null) {
                        if (listResultInfo.getData().getLists() != null && listResultInfo.getData().getLists().size() > 0) {
                            mView.showUploadBookList(listResultInfo.getData().getLists());
                            mView.hide();
                        } else {
                            if (page == 1)
                                mView.showNoData();
                        }
                    } else {
                        if (page == 1) {
                            mView.showNoData();
                        }
                    }


                } else {
                    if (page == 1)
                        mView.showNoNet();
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}

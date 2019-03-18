package com.yc.ac.index.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.ac.index.contract.BookContract;
import com.yc.ac.index.model.bean.BookInfoWrapper;
import com.yc.ac.index.model.engine.IndexBookEngine;
import com.yc.ac.utils.EngineUtils;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin  on 2018/3/8 09:15.
 */

public class BookPresenter extends BasePresenter<IndexBookEngine, BookContract.View> implements BookContract.Presenter {
    public BookPresenter(Context context, BookContract.View view) {
        super(context, view);
        mEngine = new IndexBookEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {

    }

    public void getBookList(final int page, int limit, String name, String code, String grade_id, String grade,
                            String part_type_id, String part_type, String version_id, String version, String subject_id,
                            String subject, String flag_id, String year, String latitude, String longitude) {
        if (page == 1)
            mView.showLoading();
        Subscription subscription = EngineUtils.getBookInfoList(mContext, page, limit, name, code, grade_id, grade, part_type_id, part_type, version_id, version, subject_id, subject, flag_id, year, latitude, longitude).subscribe(new Subscriber<ResultInfo<BookInfoWrapper>>() {
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
                    if (bookInfoWrapperResultInfo.getCode() == HttpConfig.STATUS_OK && bookInfoWrapperResultInfo.getData() != null && bookInfoWrapperResultInfo.getData().getLists() != null) {

                        mView.hide();
                        mView.showBookList(bookInfoWrapperResultInfo.getData());

                    } else {
                        if (page == 1) {
                            mView.showNoData();
                        } else {
                            mView.showEnd();
                        }
                    }
                } else {
                    if (page == 1) {
                        mView.showNoNet();
                    } else {
                        mView.showEnd();
                    }
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}

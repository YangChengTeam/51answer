package com.yc.answer.index.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.answer.index.contract.BookConditionContract;
import com.yc.answer.index.model.bean.VersionDetailInfo;
import com.yc.answer.index.model.engine.BookConditionEngine;
import com.yc.answer.utils.EngineUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin  on 2018/3/12 08:58.
 */

public class BookConditionPresenter extends BasePresenter<BookConditionEngine, BookConditionContract.View> implements BookConditionContract.Presenter {
    public BookConditionPresenter(Context context, BookConditionContract.View view) {
        super(context, view);
        mEngine = new BookConditionEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {
        if (!isForceUI) return;
        getConditionList();
    }

    public void getConditionList() {
        mView.showLoading();
        Subscription subscription = EngineUtils.getConditionList(mContext).subscribe(new Subscriber<ResultInfo<List<String>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(ResultInfo<List<String>> listResultInfo) {
                if (listResultInfo != null) {
                    if (listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {
                        mView.hide();
                        mView.showConditionList(listResultInfo.data);
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

    private void createNewData(List<String> list) {
        if (list != null && list.size() > 0) {
            List<VersionDetailInfo> datas = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                datas.add(new VersionDetailInfo((i + 1) + "", list.get(i)));
            }

        }

    }
}

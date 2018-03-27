package com.yc.answer.index.presenter;

import android.content.Context;

import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.vondear.rxtools.RxNetTool;
import com.yc.answer.constant.BusAction;
import com.yc.answer.index.contract.AnswerDetailContract;
import com.yc.answer.index.model.bean.BookInfo;
import com.yc.answer.index.model.engine.AnswerDetailEngine;
import com.yc.answer.utils.EngineUtils;
import com.yc.answer.utils.FileUtils;
import com.yc.answer.utils.ToastUtils;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin  on 2018/3/12 10:55.
 */

public class AnswerDetailPresenter extends BasePresenter<AnswerDetailEngine, AnswerDetailContract.View> implements AnswerDetailContract.Presenter {
    public AnswerDetailPresenter(Context context, AnswerDetailContract.View view) {
        super(context, view);
        mEngine = new AnswerDetailEngine(context);

    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {

    }


    public void getAnswerDetailInfo(final String book_id, final boolean isReload) {

        if (!isReload) {
            mView.showLoading();
        }
        final Subscription subscription = EngineUtils.getBookDetailInfo(mContext, book_id).subscribe(new Subscriber<ResultInfo<BookInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(ResultInfo<BookInfo> bookInfoResultInfo) {
                if (bookInfoResultInfo != null) {
                    if (bookInfoResultInfo.code == HttpConfig.STATUS_OK && bookInfoResultInfo.data != null) {
                        mView.hide();
                        mView.showAnswerDetailInfo(bookInfoResultInfo.data, isReload);
                    } else {
                        if (!isReload) {
                            mView.showNoData();
                        }
                    }
                } else {
                    if (!isReload) {
                        mView.showNoNet();
                    }
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    public void favoriteAnswer(final BookInfo bookInfo) {
        if (!RxNetTool.isAvailable(mContext)) {
            ToastUtils.showCenterToast(mContext, "网络错误，请检查网络");
            return;
        }

        if (bookInfo == null) {
            ToastUtils.showCenterToast(mContext, "收藏的答案为空");
            return;
        }
        Subscription subscription = mEngine.favoriteAnswer(bookInfo.getId()).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                boolean isCollect = bookInfo.getFavorite() == 1;
                if (stringResultInfo != null && stringResultInfo.code == HttpConfig.STATUS_OK && stringResultInfo.data != null) {
                    isCollect = !isCollect;
                    mView.showFavoriteResult(stringResultInfo.data, isCollect);
                    bookInfo.setFavorite(isCollect ? 1 : 0);
                    RxBus.get().post(BusAction.COLLECT, "collect");
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}

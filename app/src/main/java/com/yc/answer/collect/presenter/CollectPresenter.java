package com.yc.answer.collect.presenter;

import android.content.Context;
import android.text.Html;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.answer.base.MyApp;
import com.yc.answer.collect.contract.CollectContract;
import com.yc.answer.collect.model.engine.CollectEngine;
import com.yc.answer.constant.HttpStatus;
import com.yc.answer.index.model.bean.BookInfo;
import com.yc.answer.index.model.bean.BookInfoDao;
import com.yc.answer.index.model.bean.BookInfoWrapper;

import java.util.List;


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
                        List<BookInfo> bookInfos = queryCollectBooks();
                        if (bookInfos != null && bookInfos.size() > 0) {
                            BookInfoWrapper wrapper = new BookInfoWrapper();
                            wrapper.setCount(bookInfos.size());
                            wrapper.setLists(bookInfos);
                            mView.hide();
                            mView.showCollectList(wrapper);
                            return;
                        }

                        if (bookInfoWrapperResultInfo.code == HttpStatus.TOKEN_EXPIRED) {
//                            ToastUtils.showCenterToast(mContext, "请先登录");

                            mView.showTintInfo(Html.fromHtml("查看已收藏书籍，请先<font color=\"#FF0000\">登录</font>"));
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


    private List<BookInfo> queryCollectBooks() {
        BookInfoDao dao = MyApp.getDaoSession().getBookInfoDao();

        return dao.queryBuilder().orderDesc(BookInfoDao.Properties.SaveTime).build().list();

    }
}

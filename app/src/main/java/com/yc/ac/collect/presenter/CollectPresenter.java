package com.yc.ac.collect.presenter;

import android.content.Context;
import android.text.Html;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.ac.base.MyApp;
import com.yc.ac.collect.contract.CollectContract;
import com.yc.ac.collect.model.engine.CollectEngine;
import com.yc.ac.constant.HttpStatus;
import com.yc.ac.index.model.bean.BookInfo;
import com.yc.ac.index.model.bean.BookInfoDao;
import com.yc.ac.index.model.bean.BookInfoWrapper;

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
                    if (bookInfoWrapperResultInfo.getCode() == HttpConfig.STATUS_OK && bookInfoWrapperResultInfo.getData() != null && bookInfoWrapperResultInfo.getData().getLists() != null) {
                        mView.hide();
                        mView.showCollectList(bookInfoWrapperResultInfo.getData());
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

                        if (bookInfoWrapperResultInfo.getCode() == HttpStatus.TOKEN_EXPIRED) {
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
                    List<BookInfo> bookInfos = queryCollectBooks();
                    if (bookInfos != null && bookInfos.size() > 0) {
                        BookInfoWrapper wrapper = new BookInfoWrapper();
                        wrapper.setCount(bookInfos.size());
                        wrapper.setLists(bookInfos);
                        mView.hide();
                        mView.showCollectList(wrapper);
                        return;
                    }
                    if (page == 1) mView.showNoNet();
                }
            }
        });

        mSubscriptions.add(subscription);

    }


    private List<BookInfo> queryCollectBooks() {

        BookInfoDao infoDao = MyApp.getDaoSession().getBookInfoDao();

        return infoDao.queryBuilder().orderDesc(BookInfoDao.Properties.SaveTime).build().list();

    }
}

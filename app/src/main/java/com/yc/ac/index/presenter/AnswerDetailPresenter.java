package com.yc.ac.index.presenter;

import android.content.Context;

import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.LogUtil;
import com.vondear.rxtools.RxNetTool;
import com.yc.ac.base.MyApp;
import com.yc.ac.constant.BusAction;
import com.yc.ac.index.contract.AnswerDetailContract;
import com.yc.ac.index.model.bean.BookInfo;
import com.yc.ac.index.model.bean.BookInfoDao;
import com.yc.ac.index.model.engine.AnswerDetailEngine;
import com.yc.ac.setting.model.bean.ShareInfo;
import com.yc.ac.setting.model.bean.ShareInfoDao;
import com.yc.ac.utils.EngineUtils;
import com.yc.ac.utils.ToastUtils;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin  on 2018/3/12 10:55.
 */

public class AnswerDetailPresenter extends BasePresenter<AnswerDetailEngine, AnswerDetailContract.View> implements AnswerDetailContract.Presenter {
    private BookInfoDao infoDao;

    public AnswerDetailPresenter(Context context, AnswerDetailContract.View view) {
        super(context, view);
        mEngine = new AnswerDetailEngine(context);

        infoDao = MyApp.getDaoSession().getBookInfoDao();
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
                        BookInfo bookInfo = bookInfoResultInfo.data;
                        try {
                            bookInfo.setId(Long.parseLong(bookInfo.getBookId()));
                        } catch (Exception e) {
                            LogUtil.msg("error:  " + e.getMessage());
                        }
                        if (queryBook(bookInfo)) {
                            bookInfo.setFavorite(1);
                        }
                        if (queryShareBook(book_id)) {
                            bookInfo.setAccess(1);
                        }
                        mView.showAnswerDetailInfo(bookInfo, isReload);
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
        Subscription subscription = mEngine.favoriteAnswer(bookInfo.getBookId()).subscribe(new Subscriber<ResultInfo<String>>() {
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


    public void saveBook(BookInfo bookInfo) {
        if (bookInfo == null) {
            ToastUtils.showCenterToast(mContext, "收藏的答案为空");
            return;
        }

        boolean isCollect;

        if (queryBook(bookInfo)) {
            //删除
            infoDao.delete(bookInfo);
            bookInfo.setFavorite(0);
            isCollect = false;
        } else {
            //保存
            bookInfo.setSaveTime(System.currentTimeMillis());
            infoDao.insert(bookInfo);
            bookInfo.setFavorite(1);
            isCollect = true;
        }
        mView.showFavoriteResult("", isCollect);
        RxBus.get().post(BusAction.COLLECT, "collect");

    }


    private boolean queryBook(BookInfo bookInfo) {

        BookInfo result = infoDao.queryBuilder().where(BookInfoDao.Properties.BookId.eq(bookInfo.getBookId())).build().unique();
        return result != null;
    }

    private boolean queryShareBook(String bookId) {
        ShareInfoDao shareInfoDao = MyApp.getDaoSession().getShareInfoDao();
        ShareInfo result = shareInfoDao.queryBuilder().where(ShareInfoDao.Properties.Book_id.eq(bookId)).build().unique();
        return result != null;
    }


}

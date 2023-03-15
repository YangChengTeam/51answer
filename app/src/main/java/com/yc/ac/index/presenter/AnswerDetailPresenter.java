package com.yc.ac.index.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.LogUtil;
import com.kk.utils.VUiKit;
import com.vondear.rxtools.RxNetTool;
import com.vondear.rxtools.RxTimeTool;
import com.yc.ac.base.MyApp;
import com.yc.ac.constant.BusAction;
import com.yc.ac.index.contract.AnswerDetailContract;
import com.yc.ac.index.model.bean.BookInfo;
import com.yc.ac.index.model.bean.BookInfoDao;
import com.yc.ac.index.model.engine.AnswerDetailEngine;
import com.yc.ac.setting.model.bean.BrowserInfo;
import com.yc.ac.setting.model.bean.BrowserInfoDao;
import com.yc.ac.setting.model.bean.ShareInfo;
import com.yc.ac.setting.model.bean.ShareInfoDao;
import com.yc.ac.utils.CacheDataUtils;
import com.yc.ac.utils.EngineUtils;
import com.yc.ac.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin  on 2018/3/12 10:55.
 */

public class AnswerDetailPresenter extends BasePresenter<AnswerDetailEngine, AnswerDetailContract.View> implements AnswerDetailContract.Presenter {
    private BookInfoDao infoDao;
    private BrowserInfoDao browserInfoDao;

    public AnswerDetailPresenter(Context context, AnswerDetailContract.View view) {
        super(context, view);
        mEngine = new AnswerDetailEngine(context);

        infoDao = MyApp.getDaoSession().getBookInfoDao();
        browserInfoDao = MyApp.getDaoSession().getBrowserInfoDao();
        shareInfoDao = MyApp.getDaoSession().getShareInfoDao();
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
                    if (bookInfoResultInfo.getCode() == HttpConfig.STATUS_OK && bookInfoResultInfo.getData() != null) {
                        mView.hide();
                        BookInfo bookInfo = bookInfoResultInfo.getData();
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
                if (stringResultInfo != null && stringResultInfo.getCode() == HttpConfig.STATUS_OK && stringResultInfo.getData() != null) {
                    isCollect = !isCollect;
                    mView.showFavoriteResult(stringResultInfo.getData(), isCollect);
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
        String shareBook = CacheDataUtils.getInstance().getShareBook(bookId);
        if (!TextUtils.isEmpty(shareBook)){
               return true;
        }else {
            return false;
        }
    }


    public void saveBrowserInfo(BrowserInfo browserInfo, int page) {
        if (browserInfo == null) return;
        long currentTime = System.currentTimeMillis();
        String time = RxTimeTool.date2String(new Date(currentTime), new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));

        BrowserInfo queryBrowserInfo = queryBrowserInfo(browserInfo.getBookId());

        if (queryBrowserInfo != null) {
            queryBrowserInfo.setSaveTime(currentTime);
            queryBrowserInfo.setLastPage(page);
            browserInfoDao.update(queryBrowserInfo);
        } else {
            browserInfo.setBrowserTime(time);
            browserInfo.setLastPage(page);
            browserInfo.setSaveTime(currentTime);
            browserInfoDao.insert(browserInfo);
        }


    }

    private BrowserInfo queryBrowserInfo(String bookId) {
        long currentTime = System.currentTimeMillis();
        String time = RxTimeTool.date2String(new Date(currentTime), new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));
        return browserInfoDao.queryBuilder().where(BrowserInfoDao.Properties.BookId.eq(bookId)).where(BrowserInfoDao.Properties.BrowserTime.eq(time)).build().unique();
    }


    public void share( String book_id) {
        VUiKit.postDelayed(100, new Runnable() {
            @Override
            public void run() {
                saveShare(book_id);
                mView.showSuccess();
            }
        });

       /* Subscription subscription = mEngine.share(book_id).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                Log.d("ccc", "-----000-----onNext: "+stringResultInfo.getCode() );
                if (stringResultInfo != null && stringResultInfo.getCode() == HttpConfig.STATUS_OK) {
                    Log.d("ccc", "-----222-----onNext: ");
                } else {
                    Log.d("ccc", "-----444-----onNext: ");
                    saveShare(book_id);
                }
                Log.d("ccc", "------111----onNext: ");
                mView.showSuccess();
            }
        });
        mSubscriptions.add(subscription);*/
    }

    private ShareInfoDao shareInfoDao;
    private void saveShare(String bookiD) {
        CacheDataUtils.getInstance().setShareBook(bookiD);
    }
}

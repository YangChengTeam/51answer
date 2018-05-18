package com.yc.answer.index.presenter;

import android.content.Context;

import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.vondear.rxtools.RxNetTool;
import com.yc.answer.constant.BusAction;
import com.yc.answer.index.contract.BookConditionContract;
import com.yc.answer.index.model.bean.BookInfo;
import com.yc.answer.index.model.bean.BookInfoWrapper;
import com.yc.answer.index.model.bean.VersionDetailInfo;
import com.yc.answer.index.model.engine.BookConditionEngine;
import com.yc.answer.utils.EngineUtils;
import com.yc.answer.utils.ToastUtils;

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

//        getConditionList();
    }

    public void getConditionList() {

        Subscription subscription = EngineUtils.getConditionList(mContext).subscribe(new Subscriber<ResultInfo<List<String>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

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

    public void getBookList(final int page, int limit, String name, String code, String grade_id, String grade,
                            String part_type_id, String part_type, String version_id, String version, String subject_id,
                            String subject, String flag_id, String year) {
        if (page == 1)
            mView.showLoading();
        Subscription subscription = EngineUtils.getBookInfoList(mContext, page, limit, name, code, grade_id, grade, part_type_id, part_type, version_id, version, subject_id, subject, flag_id, year, "", "").subscribe(new Subscriber<ResultInfo<BookInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (page == 1)
                    mView.showNoNet();
            }

            @Override
            public void onNext(ResultInfo<BookInfoWrapper> bookInfoWrapperResultInfo) {
                if (bookInfoWrapperResultInfo != null && bookInfoWrapperResultInfo.code == HttpConfig.STATUS_OK) {
                    if (bookInfoWrapperResultInfo.data != null && bookInfoWrapperResultInfo.data.getLists() != null) {
                        mView.hide();
                        mView.showBookInfoList(bookInfoWrapperResultInfo.data.getLists());
                    } else {
                        if (page == 1)
                            mView.showNoData();
                    }
                } else {
                    if (page == 1)
                        mView.showNoNet();
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
                if (stringResultInfo != null) {
                    if (stringResultInfo.code == HttpConfig.STATUS_OK && stringResultInfo.data != null) {
                        isCollect = !isCollect;
                        mView.showFavoriteResult(isCollect);
                        bookInfo.setFavorite(isCollect ? 1 : 0);
                        RxBus.get().post(BusAction.COLLECT, "list");
                        ToastUtils.showCenterToast(mContext, (isCollect ? "收藏" : "取消收藏") + "成功");
                    } else {
                        ToastUtils.showCenterToast(mContext, stringResultInfo.message);
                    }

                } else {
                    ToastUtils.showCenterToast(mContext, HttpConfig.NET_ERROR);
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}

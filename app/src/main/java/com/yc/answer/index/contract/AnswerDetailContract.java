package com.yc.answer.index.contract;

import com.yc.answer.index.model.bean.BookInfo;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2018/3/12 10:54.
 */

public interface AnswerDetailContract {
    interface View extends IView, IHide, INoNet, INoData, ILoading {
        void showAnswerDetailInfo(BookInfo data, boolean isReload);

        void showFavoriteResult(String data, boolean isCollect);
    }

    interface Presenter extends IPresenter {
    }
}

package com.yc.answer.index.contract;

import com.yc.answer.index.model.bean.BookInfo;

import java.util.List;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2018/4/24 16:12.
 */

public interface UploadBookListContract {
    interface View extends IView, ILoading, INoData, INoNet, IHide {
        void showUploadBookList(List<BookInfo> data);
    }

    interface Presenter extends IPresenter {
    }
}

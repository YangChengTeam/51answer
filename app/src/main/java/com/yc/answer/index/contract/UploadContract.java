package com.yc.answer.index.contract;

import com.yc.answer.index.model.bean.BookInfo;

import yc.com.base.IDialog;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2018/4/23 16:08.
 */

public interface UploadContract {
    interface View extends IView, IDialog {
        void showUploadResult(BookInfo body);
    }

    interface Presenter extends IPresenter {
    }
}

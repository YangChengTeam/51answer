package com.yc.ac.collect.contract;

import com.yc.ac.index.model.bean.BookInfoWrapper;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2018/3/8 18:50.
 */

public interface CollectContract {

    interface View extends IView,ILoading,IHide,INoNet,INoData {
        void showCollectList(BookInfoWrapper data);

        void showEnd();

        void showTintInfo(CharSequence spanned);
    }

    interface Presenter extends IPresenter {
    }

}

package com.yc.ac.index.contract;

import android.content.Intent;

import com.yc.ac.index.model.bean.BookInfo;
import com.yc.ac.index.model.bean.BookInfoWrapper;

import java.util.List;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2018/3/8 09:16.
 */

public interface BookContract {

    interface View extends IView, ILoading, INoData, INoNet, IHide {


        void showBookList(BookInfoWrapper data);

        void showEnd();
    }

    interface Presenter extends IPresenter {
    }
}

package com.yc.ac.index.contract;


import java.util.List;

import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2018/3/20 13:35.
 */

public interface SearchContract {
    interface View extends IView {
        void showSearchTips(List<String> data);
    }

    interface Presenter extends IPresenter {
    }
}

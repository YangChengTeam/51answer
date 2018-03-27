package com.yc.answer.setting.contract;


import yc.com.base.IDialog;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2018/3/12 19:15.
 */

public interface SettingContract {
    interface View extends IView,IDialog {
        void showLogout();

        void showCacheSize(String cacheSize);
    }

    interface Presenter extends IPresenter {
    }
}

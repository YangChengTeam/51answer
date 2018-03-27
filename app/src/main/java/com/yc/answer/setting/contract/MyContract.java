package com.yc.answer.setting.contract;

import com.yc.answer.setting.model.bean.UserInfo;

import yc.com.base.IDialog;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2018/3/12 17:14.
 */

public interface MyContract {
    interface View extends IView,IDialog {
        void showNotLogin(String b);

        void showUserInfo(UserInfo userInfo);
    }

    interface Presenter extends IPresenter {
        void getUserInfo();
    }
}

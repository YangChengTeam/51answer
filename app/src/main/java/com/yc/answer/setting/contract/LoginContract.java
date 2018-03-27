package com.yc.answer.setting.contract;


import com.yc.answer.setting.model.bean.UserInfo;

import yc.com.base.IDialog;
import yc.com.base.IFinish;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2018/3/7 15:10.
 */

public interface LoginContract {
    interface View extends IView, IDialog, IFinish {
        void showErrorAccount();

        void showErrorPassword();

        void showErrorCode();

        void showGetCode();

        void showAccountResult(UserInfo userInfo, String string);
    }

    interface Presenter extends IPresenter {
    }
}

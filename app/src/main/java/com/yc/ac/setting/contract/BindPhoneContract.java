package com.yc.ac.setting.contract;


import yc.com.base.IDialog;
import yc.com.base.IFinish;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2018/3/15 10:50.
 */

public interface BindPhoneContract {

    interface View extends IView, IDialog, IFinish {

        void showErrorAccount();

        void showErrorCode();

        void showGetCode();
    }

    interface Presenter extends IPresenter {
        void changePhone(String mobile, String code);

        void bindPhone(String mobile, String code);
    }
}

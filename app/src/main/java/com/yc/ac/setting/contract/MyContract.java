package com.yc.ac.setting.contract;

import com.yc.ac.setting.model.bean.QbInfo;
import com.yc.ac.setting.model.bean.TaskListInfo;
import com.yc.ac.setting.model.bean.UserInfo;

import java.util.List;

import yc.com.base.IDialog;
import yc.com.base.IHide;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2018/3/12 17:14.
 */

public interface MyContract {
    interface View extends IView, IDialog, INoData, INoNet, IHide {
        void showNotLogin(String b);

        void showUserInfo(UserInfo userInfo);

    }

    interface Presenter extends IPresenter {
        void getUserInfo();
    }
}

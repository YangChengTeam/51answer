package com.yc.ac.setting.contract;

import com.yc.ac.setting.model.bean.QbInfo;
import com.yc.ac.setting.model.bean.TaskListInfo;
import com.yc.ac.setting.model.bean.UserInfo;

import java.util.List;

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

        void showQbInfo(QbInfo info);

        void showTaskList(List<TaskListInfo> list);
    }

    interface Presenter extends IPresenter {
        void getUserInfo();
    }
}

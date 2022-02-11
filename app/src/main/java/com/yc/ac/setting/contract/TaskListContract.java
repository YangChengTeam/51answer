package com.yc.ac.setting.contract;

import com.yc.ac.setting.model.bean.TaskListInfo;

import java.util.List;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2018/9/13 17:41.
 */
public interface TaskListContract {

    interface View extends IView, INoData, INoNet, IHide, ILoading {
        void showTaskList(List<TaskListInfo> list);
    }


    interface Presenter extends IPresenter {
        void getTaskInfoList(boolean isReload);
    }
}

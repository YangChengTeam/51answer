package com.yc.answer.index.contract;

import yc.com.base.IDialog;
import yc.com.base.IFinish;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2018/4/24 15:28.
 */

public interface PerfectBookContract {
    interface View extends IView,IDialog,IFinish{}
    interface Presenter extends IPresenter{}
}

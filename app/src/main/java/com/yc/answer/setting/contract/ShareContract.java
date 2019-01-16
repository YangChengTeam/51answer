package com.yc.answer.setting.contract;

import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2018/3/22 10:32.
 */

public interface ShareContract {

    interface View extends IView {

        void showSuccess();
    }


    interface Presenter extends IPresenter {}
}

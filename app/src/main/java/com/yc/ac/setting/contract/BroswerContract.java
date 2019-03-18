package com.yc.ac.setting.contract;

import com.yc.ac.setting.model.bean.BrowserInfo;

import java.util.List;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2019/3/15 10:43.
 */
public interface BroswerContract {
    interface View extends IView ,ILoading,INoData,INoNet,IHide{
        void showBrowserInfos(List<BrowserInfo> browserInfos);

        void showEnd();
    }

    interface Presenter extends IPresenter {
        void getBrowserInfos(final int page, final int pageSize);
    }
}

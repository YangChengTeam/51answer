package com.yc.ac.setting.contract;

import com.yc.ac.setting.model.bean.OrderInfo;

import java.util.List;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;

public interface OrderListContract {
    interface View extends IView, ILoading, IHide, INoData,INoNet {
        void showOrderInfoList(List<OrderInfo> data);
    }

    interface Presenter extends IPresenter {
        void getOrderInfoList(boolean b);
    }
}

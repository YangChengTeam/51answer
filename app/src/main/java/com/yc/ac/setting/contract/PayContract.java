package com.yc.ac.setting.contract;

import com.yc.ac.index.model.bean.PayItemInfo;
import com.yc.ac.pay.PayInfo;
import com.yc.ac.setting.model.engine.WxPayInfo;

import java.util.List;

import yc.com.base.IDialog;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin on 2021/5/19 17:26
 */
public interface PayContract {
    interface View extends IView, IDialog {
        void showPayItemInfos(List<PayItemInfo> data);

        void showAliPayInfo(PayInfo data);

        void showWxPayInfo(WxPayInfo data);
    }

    interface Presenter extends IPresenter {
        void getPayItemInfoList();

        void aliPay(String userId, String goodsId, String classId, String articleId);

        void wxPay(String userId, String goodsId, String classId, String articleId);

    }
}

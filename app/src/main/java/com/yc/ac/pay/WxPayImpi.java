package com.yc.ac.pay;

import android.app.Activity;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yc.ac.setting.model.engine.OrderParamsInfo;
import com.yc.ac.setting.model.engine.WxPayInfo;
import com.yc.ac.utils.ToastUtils;

/**
 * Created by wanglin on 2021/5/20 10:04
 */
public class WxPayImpi {
    private final IWXAPI msgApi;
    private Activity mActivity;

    public WxPayImpi(Activity activity) {
        this.mActivity = activity;
        msgApi = WXAPIFactory.createWXAPI(activity, null);
    }

    public void pay(OrderParamsInfo payInfo) {
        if (payInfo == null) {
            ToastUtils.showCenterToast(mActivity, "参数错误");
            return;
        }
        if ("SUCCESS".equals(payInfo.getReturn_code())) {
            Constants.APP_ID = payInfo.getAppid();
            msgApi.registerApp(payInfo.getAppid());

            PayReq request = new PayReq();
            request.appId = payInfo.getAppid();
            request.partnerId = payInfo.getMch_id();
            request.prepayId = payInfo.getPrepay_id();
            request.packageValue = "Sign=WXPay";
            request.nonceStr = payInfo.getNonce_str();
            request.timeStamp = payInfo.getTimestamp() + "";
            request.sign = payInfo.getSign();
//            request.signType = "HMACSHA256";
            msgApi.sendReq(request);
        } else {
            ToastUtils.showCenterToast(mActivity, "预付款下单失败");
        }


    }
}

package com.yc.ac.pay;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.alipay.sdk.app.PayTask;
import com.vondear.rxtools.module.alipay.PayResult;
import com.yc.ac.R;
import com.yc.ac.utils.ToastUtils;

import java.util.Map;

/**
 * Created by wanglin on 2021/5/19 19:27
 */
public class AlipayImpi {
    private static final int SDK_PAY_FLAG = 100;
    private PayResultListener mPayResultListener;
    private Activity mActivity;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == SDK_PAY_FLAG) {
                @SuppressWarnings("unchecked")
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                /**
                 * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {//支付成功
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。

                    if (mPayResultListener != null) {
                        mPayResultListener.paySuccess(resultInfo);
                    }
                    ToastUtils.showCenterToast(mActivity, "支付成功");
                } else if (TextUtils.equals(resultStatus, "6001")) {//支付取消
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    if (mPayResultListener != null) {
                        mPayResultListener.payCancel();
                    }
                    ToastUtils.showCenterToast(mActivity, "支付取消");
                } else {//支付失败
                    if (mPayResultListener != null) {
                        mPayResultListener.payFailure(resultInfo);
                    }
                    ToastUtils.showCenterToast(mActivity, "支付失败");
                }
            }
        }
    };

    public AlipayImpi(Activity activity, PayResultListener payResultListener) {
        this.mActivity = activity;
        this.mPayResultListener = payResultListener;
    }

    public void pay(String orderInfo) {
        Runnable payRunnable = () -> {
            PayTask alipay = new PayTask(mActivity);
            Map<String, String> result = alipay.payV2(orderInfo, true);

            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}

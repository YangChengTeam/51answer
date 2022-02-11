package com.yc.ac.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yc.ac.constant.BusAction;
import com.yc.ac.pay.Constants;
import com.yc.ac.pay.PayResultInfo;
import com.yc.ac.utils.ToastUtils;

/**
 * Created by wanglin on 2021/5/20 10:13
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", "onCreate: " + Constants.APP_ID);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }
//wx2f4167354ae3499d  1607808066 1607808066

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int errCode = resp.errCode;
//            resp.
            String mess = "未知错误";
            switch (errCode) {
                case 0://支付成功
                    mess = "支付成功";
                    break;
                case -1://错误
                    mess = "支付失败:" + resp.errStr;
                    break;
                case -2://用户取消
                    mess = "支付取消";
                    break;
                default:
                    break;
            }
            ToastUtils.showCenterToast(this, mess);
            RxBus.get().post(BusAction.WX_PAY_SUCCESS, new PayResultInfo(errCode, mess));
            finish();
        }
    }
}

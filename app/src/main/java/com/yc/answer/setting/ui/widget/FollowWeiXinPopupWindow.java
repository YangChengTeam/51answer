package com.yc.answer.setting.ui.widget;

import android.app.Activity;
import android.widget.TextView;


import com.jakewharton.rxbinding.view.RxView;
import com.vondear.rxtools.RxAppTool;
import com.vondear.rxtools.RxClipboardTool;
import com.yc.answer.R;
import com.yc.answer.utils.ToastUtils;
import com.yc.answer.utils.UIUtils;


import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BasePopwindow;

/**
 * Created by zhangkai on 2017/8/2.
 */

public class FollowWeiXinPopupWindow extends BasePopwindow {

    @BindView(R.id.tv_ok)
    TextView mOkTextView;

    @BindView(R.id.tv_cancel)
    TextView mCancelTextView;

    public FollowWeiXinPopupWindow(Activity context) {
        super(context);
    }

    @Override
    public void init() {
        RxView.clicks(mOkTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
                RxClipboardTool.copyText(mContext, mContext.getString(R.string.app_name));
                ToastUtils.showCenterToast(mContext, "复制成功, 正在前往微信");
                UIUtils.postDelay(new Runnable() {
                    @Override
                    public void run() {
                        String weixin = "com.tencent.mm";
                        if (RxAppTool.isInstallApp(mContext, weixin)) {
                            RxAppTool.launchApp(mContext, weixin);
                        }
                    }
                }, 1000);
            }
        });

        RxView.clicks(mCancelTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.setting_ppw_follow_weixin;
    }

    @Override
    public int getAnimationID() {
        return R.style.share_anim;
    }
}

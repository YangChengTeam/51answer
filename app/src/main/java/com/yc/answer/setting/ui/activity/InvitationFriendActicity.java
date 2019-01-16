package com.yc.answer.setting.ui.activity;

import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ToastUtil;
import com.yc.answer.R;
import com.yc.answer.utils.ToastUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/5/21 17:25.
 */

public class InvitationFriendActicity extends BaseActivity {
    @BindView(R.id.tv_invitation_code)
    TextView tvInvitationCode;
    @BindView(R.id.iv_invitation_btn)
    ImageView ivInvitationBtn;

    private String code = "XYHG5EHD67";

    @Override
    public int getLayoutId() {
        return R.layout.activity_invitation_friend;
    }

    @Override
    public void init() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < code.getBytes().length; i++) {
            sb.append(code.charAt(i)).append(" ");
            if (i == code.getBytes().length - 1) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        tvInvitationCode.setText(sb.toString());
        initListener();

    }

    private void initListener() {
        RxView.clicks(ivInvitationBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ToastUtils.showCenterToast(InvitationFriendActicity.this, "该功能正在完善中");
            }
        });
    }


}

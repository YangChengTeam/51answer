package com.yc.answer.setting.ui.activity;

import android.widget.ImageView;
import android.widget.TextView;

import com.yc.answer.R;

import butterknife.BindView;
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

    }


}

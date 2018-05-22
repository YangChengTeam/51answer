package com.yc.answer.setting.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.vondear.rxtools.RxSPTool;
import com.yc.answer.R;
import com.yc.answer.constant.SpConstant;
import com.yc.answer.setting.ui.fragment.ShareFragment;
import com.yc.answer.setting.ui.widget.BaseSettingView;
import com.yc.answer.utils.ToastUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/5/21 15:30.
 */

public class EarningsDetailActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;
    @BindView(R.id.baseSettingView_new_book)
    BaseSettingView baseSettingViewNewBook;
    @BindView(R.id.baseSettingView_share)
    BaseSettingView baseSettingViewShare;
    @BindView(R.id.baseSettingView_market)
    BaseSettingView baseSettingViewMarket;
    @BindView(R.id.baseSettingView_friend)
    BaseSettingView baseSettingViewFriend;

    private long startTime;

    private String done = "已完成";
    private String gotoDone = "去完成";

    @Override
    public int getLayoutId() {
        return R.layout.activity_earning_detail;
    }

    @Override
    public void init() {
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        commonTvTitle.setText(getString(R.string.look_detail));
        boolean istoMarket = RxSPTool.getBoolean(this, SpConstant.OPEN_MARKET);
        setState(istoMarket, baseSettingViewMarket);

        initListener();
    }


    private void setState(boolean isOk, BaseSettingView baseSettingView) {
        baseSettingView.setExtraText(isOk ? done : gotoDone);
        baseSettingView.setExtraColor(isOk ? ContextCompat.getColor(this, R.color.green_4ec54e) : ContextCompat.getColor(this, R.color.gray_999));
    }

    private void initListener() {
        RxView.clicks(baseSettingViewMarket).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startTime = System.currentTimeMillis();
                //好评赚钱
                try {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showCenterToast(EarningsDetailActivity.this, "你手机安装的应用市场没有上线该应用，请前往其他应用市场进行点评");
                }
            }
        });
        RxView.clicks(baseSettingViewShare).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ShareFragment shareFragment = new ShareFragment();
                shareFragment.show(getSupportFragmentManager(), "");
            }
        });

        RxView.clicks(baseSettingViewFriend).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(EarningsDetailActivity.this, InvitationFriendActicity.class));
            }
        });
        RxView.clicks(baseSettingViewNewBook).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(EarningsDetailActivity.this, UploadBookIntroduceActivity.class));
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (startTime > 0) {
            if ((System.currentTimeMillis() - startTime) / 1000 >= 5) {
                //跳到应用市场
                RxSPTool.putBoolean(this, SpConstant.OPEN_MARKET, true);
                baseSettingViewMarket.setExtraText("已完成");
                baseSettingViewMarket.setExtraColor(ContextCompat.getColor(this, R.color.green_4ec54e));
            }
        }
        boolean isShareSuccess = RxSPTool.getBoolean(this, SpConstant.SHARE_SUCCESS);
        setState(isShareSuccess, baseSettingViewShare);

    }

}

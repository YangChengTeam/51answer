package com.yc.ac.setting.ui.activity;

import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.ac.R;
import com.yc.ac.setting.ui.adapter.NotificationAdapter;
import com.yc.ac.setting.ui.fragment.NotificationFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import yc.com.base.BaseActivity;

public class NotificationActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    //    @BindView(R.id.content_group)
//    Group contentGroup;
    @BindView(R.id.empty_group)
    Group emptyGroup;
    @BindView(R.id.viewpager_notification)
    ViewPager viewpagerNotification;

    @Override
    public int getLayoutId() {
        return R.layout.activity_notification;
    }

    @Override
    public void init() {
        commonTvTitle.setText("消息通知");
        initViewPager();
        initListener();
    }

    private void initListener() {
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> finish());

    }

    private void initViewPager() {
        List<String> titleList = new ArrayList<>();
        titleList.add("全部(6)");
        titleList.add("未读(16)");
        List<Fragment> fragments = new ArrayList<>();
        for (String s : titleList) {
            fragments.add(new NotificationFragment());
        }
        NotificationAdapter notificationAdapter = new NotificationAdapter(getSupportFragmentManager(), fragments, titleList);
        viewpagerNotification.setAdapter(notificationAdapter);
        tabLayout.setupWithViewPager(viewpagerNotification);
    }

}

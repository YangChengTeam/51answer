package com.yc.ac.setting.ui.fragment;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yc.ac.R;
import com.yc.ac.setting.model.bean.NotificationInfo;
import com.yc.ac.setting.ui.adapter.NotificationItemAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import yc.com.base.BaseFragment;

/**
 * Created by wanglin on 2021/5/13 14:23
 */
public class NotificationFragment extends BaseFragment {
    @BindView(R.id.recyclerView_notification)
    RecyclerView recyclerViewNotification;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private NotificationItemAdapter notificationAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_notification;
    }

    @Override
    public void init() {
        notificationAdapter = new NotificationItemAdapter(null);
        recyclerViewNotification.setAdapter(notificationAdapter);
        initData();
        initListener();
    }

    private void initListener() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(requireActivity(), R.color.red_f14343));
        swipeRefreshLayout.setOnRefreshListener(() -> swipeRefreshLayout.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 500));
    }

    private void initData() {
        List<NotificationInfo> notificationInfos = new ArrayList<>();
        notificationInfos.add(new NotificationInfo("快来瓜分千元红包！领今日福利", 1, System.currentTimeMillis()));
        notificationInfos.add(new NotificationInfo("快来瓜分千元红包！领今日福利", 2, System.currentTimeMillis()));
        notificationInfos.add(new NotificationInfo("快来瓜分千元红包！领今日福利", 3, System.currentTimeMillis()));
        notificationAdapter.setNewData(notificationInfos);
    }
}

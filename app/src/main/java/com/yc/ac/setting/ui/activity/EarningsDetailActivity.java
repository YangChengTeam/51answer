package com.yc.ac.setting.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.vondear.rxtools.RxSPTool;
import com.yc.ac.R;
import com.yc.ac.base.StateView;
import com.yc.ac.constant.BusAction;
import com.yc.ac.constant.SpConstant;
import com.yc.ac.index.ui.widget.MyDecoration;
import com.yc.ac.setting.contract.TaskListContract;
import com.yc.ac.setting.model.bean.TaskListInfo;
import com.yc.ac.setting.presenter.TaskListPresenter;
import com.yc.ac.setting.ui.adapter.TaskListAdapter;
import com.yc.ac.setting.ui.fragment.ShareFragment;
import com.yc.ac.setting.ui.widget.BaseSettingView;
import com.yc.ac.utils.ToastUtils;
import com.yc.ac.utils.UserInfoHelper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/5/21 15:30.
 */

public class EarningsDetailActivity extends BaseActivity<TaskListPresenter> implements TaskListContract.View {
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
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
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.earning_recyclerView)
    RecyclerView earningRecyclerView;

    private long startTime;

    private String done = "已完成";
    private String gotoDone = "去完成";
    private TaskListAdapter taskListAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_earning_detail;
    }

    @Override
    public void init() {
        mPresenter = new TaskListPresenter(this, this);

        earningRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskListAdapter = new TaskListAdapter(null);

        earningRecyclerView.setAdapter(taskListAdapter);

        earningRecyclerView.addItemDecoration(new MyDecoration(10));

        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        commonTvTitle.setText(getString(R.string.look_detail));

        initListener();
    }


    private void initListener() {

        taskListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TaskListInfo taskListInfo = taskListAdapter.getItem(position);
                if (taskListInfo != null && !TextUtils.isEmpty(taskListInfo.getName())) {
                    if (taskListInfo.getName().contains("邀请")) {
                        startActivity(new Intent(EarningsDetailActivity.this, InvitationFriendActicity.class));
                    } else if (taskListInfo.getName().contains("好评")) {
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
                    } else if (taskListInfo.getName().contains("分享")) {
                        ShareFragment shareFragment = new ShareFragment();
                        shareFragment.setIsShareMoney(true);
                        shareFragment.show(getSupportFragmentManager(), "");
                    } else if (taskListInfo.getName().contains("上传")) {
                        startActivity(new Intent(EarningsDetailActivity.this, UploadBookIntroduceActivity.class));
                    }

                }
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (startTime > 0 && !RxSPTool.getBoolean(this, SpConstant.OPEN_MARKET)) {
            if ((System.currentTimeMillis() - startTime) / 1000 >= 5) {
                //跳到应用市场
                mPresenter.comment(UserInfoHelper.getUId());
            }
        }
    }

    @Override
    public void showTaskList(List<TaskListInfo> list) {
        taskListAdapter.setNewData(list);
    }


    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showLoading() {
        stateView.showLoading(earningRecyclerView);
    }

    @Override
    public void showNoData() {
        stateView.showNoData(earningRecyclerView);
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(earningRecyclerView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loadData(true, true);
            }
        });
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.SHARE_MONEY_SUCCESS)
            }
    )
    public void shareMoneySuccess(String result) {
        mPresenter.getTaskInfoList(true);
    }


}

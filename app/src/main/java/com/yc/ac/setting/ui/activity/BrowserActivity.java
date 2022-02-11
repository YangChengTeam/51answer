package com.yc.ac.setting.ui.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yc.ac.R;
import com.yc.ac.base.StateView;
import com.yc.ac.index.ui.activity.AnswerDetailActivity;
import com.yc.ac.setting.contract.BroswerContract;
import com.yc.ac.setting.model.bean.BrowserInfo;
import com.yc.ac.setting.presenter.BrowserPresenter;
import com.yc.ac.setting.ui.adapter.BrowserAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2019/3/15 10:09.
 */
public class BrowserActivity extends BaseActivity<BrowserPresenter> implements BroswerContract.View {
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;
    @BindView(R.id.tv_ok)
    TextView tvOk;

    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.browser_recyclerView)
    RecyclerView browserRecyclerView;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    private int page = 1;
    private int PAGESIZE = 10;
    private BrowserAdapter browserAdapter;
    private List<BrowserInfo> deleteInfos;

    @Override
    public int getLayoutId() {
        return R.layout.activity_browser_record;
    }

    @Override
    public void init() {
        commonTvTitle.setText(getString(R.string.browser_record));
        tvOk.setText(getString(R.string.delete));
        tvOk.setVisibility(View.VISIBLE);

        mPresenter = new BrowserPresenter(this, this);
        mPresenter.getBrowserInfos(page, PAGESIZE);
        deleteInfos = new ArrayList<>();
        browserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        browserAdapter = new BrowserAdapter(null);
        browserRecyclerView.setAdapter(browserAdapter);
//        browserRecyclerView.setItemAnimator(null);

        initRefresh();
        initListener();
    }

    private void initListener() {
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> finish());

        RxView.clicks(tvOk).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            String okText = tvOk.getText().toString();
            if (TextUtils.equals(getString(R.string.delete), okText)) {
                setSelectIconState(true);
                tvOk.setText(getString(R.string.base_ok));
            } else if (TextUtils.equals(getString(R.string.base_ok), okText)) {
                setSelectIconState(false);
                tvOk.setText(getString(R.string.delete));
                if (deleteInfos.size() > 0) {
                    for (BrowserInfo browserInfo : deleteInfos) {
                        mPresenter.deleteBrowserInfo(browserInfo);
                    }
                    page = 1;
                    mPresenter.getBrowserInfos(page, PAGESIZE);
                }

            }
        });

        browserAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            BrowserInfo item = browserAdapter.getItem(position);
            if (item != null) {
                boolean select = !item.isSelect();
                if (select) {
                    deleteInfos.add(item);
                } else {
                    deleteInfos.remove(item);
                }
                view.setSelected(select);
                item.setSelect(select);
            }

        });

        browserAdapter.setOnItemClickListener((adapter, view, position) -> {
            BrowserInfo item = browserAdapter.getItem(position);
            if (item != null) {
                AnswerDetailActivity.startActivity(BrowserActivity.this, item.getName(), item.getBookId());
            }
        });

        browserAdapter.setOnLoadMoreListener(() -> mPresenter.getBrowserInfos(page, PAGESIZE), browserRecyclerView);

    }

    private void setSelectIconState(boolean flag) {

        if (browserAdapter != null) {
            List<BrowserInfo> mDatas = browserAdapter.getData();
            for (BrowserInfo browserInfo : mDatas) {
                browserInfo.setShowIcon(flag);
            }
            browserAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void showBrowserInfos(List<BrowserInfo> browserInfos) {
        browserAdapter.setNewData(browserInfos);

        if (browserInfos.size() / page == PAGESIZE) {
            browserAdapter.loadMoreComplete();
            page++;
        } else {
            browserAdapter.loadMoreEnd();
        }
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void showEnd() {
        browserAdapter.loadMoreEnd();
    }

    private void initRefresh() {
        //设置 Header 为 BezierRadar 样式
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        smartRefreshLayout.setEnableLoadMore(false);
//        smartRefreshLayout.autoRefresh();
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            mPresenter.getBrowserInfos(page, PAGESIZE);
            tvOk.setText("删除");
            setSelectIconState(false);
        });
    }


    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showLoading() {
        stateView.showLoading(smartRefreshLayout);
    }

    @Override
    public void showNoData() {
        stateView.showNoData(smartRefreshLayout);
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(smartRefreshLayout, HttpConfig.NET_ERROR, v -> {
            page = 1;
            mPresenter.getBrowserInfos(page, PAGESIZE);
        });
        smartRefreshLayout.finishRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setSelectIconState(false);
    }
}

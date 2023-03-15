package com.yc.ac.setting.ui.activity;

import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.VUiKit;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.yc.ac.R;
import com.yc.ac.base.MyApp;
import com.yc.ac.base.StateView;
import com.yc.ac.setting.contract.OrderListContract;
import com.yc.ac.setting.model.bean.OrderInfo;
import com.yc.ac.setting.presenter.OrderListPresenter;
import com.yc.ac.setting.ui.adapter.RechargeRecordAdapter;
import com.yc.ac.utils.ItemDecorationHelper;
import com.yc.ac.utils.adgromore.GromoreNewInsetShowTwo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import yc.com.base.BaseActivity;

public class RechargeRecordActivity extends BaseActivity<OrderListPresenter> implements OrderListContract.View {
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;
    @BindView(R.id.recyclerView_recharge)
    RecyclerView recyclerViewRecharge;

    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    private RechargeRecordAdapter recordAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_recharge_record;
    }

    @Override
    public void init() {

        mPresenter = new OrderListPresenter(this, this);
        commonTvTitle.setText("充值记录");

        initRecyclerView();
        initListener();
        initRefresh();
        initData();
        showInset();
    }
    private void showInset() {

    }


    private void initRefresh() {
        //设置 Header 为 BezierRadar 样式
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.autoRefresh();
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            mPresenter.getOrderInfoList(true);
        });
    }

    private void initData() {

    }

    private void initListener() {
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MICROSECONDS).subscribe(aVoid -> finish());
    }

    private void initRecyclerView() {
        recordAdapter = new RechargeRecordAdapter(null);
        recyclerViewRecharge.setAdapter(recordAdapter);
        recyclerViewRecharge.addItemDecoration(new ItemDecorationHelper(10));
    }

    @Override
    public void showOrderInfoList(List<OrderInfo> orderInfos) {
        recordAdapter.setNewData(orderInfos);
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void showNoData() {
        stateView.showNoData(recyclerViewRecharge, "没有订单数据");
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void showNoNet() {
        stateView.showNoData(recyclerViewRecharge, "网络异常，请点击重试", v -> mPresenter.getOrderInfoList(true));
        smartRefreshLayout.finishRefresh();
    }


    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showLoading() {
        stateView.showLoading(recyclerViewRecharge);
    }


}

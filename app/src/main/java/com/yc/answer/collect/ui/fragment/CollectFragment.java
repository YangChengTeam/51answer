package com.yc.answer.collect.ui.fragment;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vondear.rxtools.RxImageTool;
import com.yc.answer.R;
import com.yc.answer.base.StateView;
import com.yc.answer.collect.contract.CollectContract;
import com.yc.answer.collect.presenter.CollectPresenter;
import com.yc.answer.constant.BusAction;
import com.yc.answer.index.model.bean.BookInfo;
import com.yc.answer.index.model.bean.BookInfoWrapper;
import com.yc.answer.index.ui.activity.AnswerDetailActivity;
import com.yc.answer.index.ui.adapter.IndexBookAdapter;
import com.yc.answer.index.ui.widget.MyDecoration;
import com.yc.answer.setting.model.bean.UserInfo;

import butterknife.BindView;
import yc.com.base.BaseFragment;

/**
 * Created by wanglin  on 2018/3/7 13:53.
 */

public class CollectFragment extends BaseFragment<CollectPresenter> implements CollectContract.View {


    @BindView(R.id.collect_recyclerView)
    RecyclerView collectRecyclerView;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;


    private IndexBookAdapter indexBookAdapter;

    private int page = 1;
    private int limit = 15;
    private TextView tvCollectCount;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_collect;
    }

    @Override
    public void init() {

        mPresenter = new CollectPresenter(getActivity(), this);
        ivBack.setVisibility(View.GONE);
        commonTvTitle.setText("我的收藏");

        collectRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        indexBookAdapter = new IndexBookAdapter(null);
        collectRecyclerView.setAdapter(indexBookAdapter);
        collectRecyclerView.addItemDecoration(new MyDecoration(10, 10));

        addHeaderView();

        initListener();
        initRefresh();
        getData(false);

    }

    private void addHeaderView() {
        View headerView = View.inflate(getActivity(), R.layout.collect_header_view, null);
        tvCollectCount = headerView.findViewById(R.id.tv_header_view);
        indexBookAdapter.addHeaderView(headerView);
    }

    private void initListener() {
        indexBookAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(false);

            }
        }, collectRecyclerView);

        indexBookAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BookInfo bookInfo = (BookInfo) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), AnswerDetailActivity.class);
                intent.putExtra("bookId", bookInfo.getId());
                intent.putExtra("bookName", bookInfo.getName());
                startActivity(intent);
            }
        });

    }


    @Override
    public void showCollectList(BookInfoWrapper data) {

        if (data != null && data.getLists() != null) {
            int count = data.getCount();

            String str = "共收藏 <font color=\"#FF0000\"> " + count + "</font> 个";

            tvCollectCount.setText(Html.fromHtml(str));

            if (page == 1) {
                indexBookAdapter.setNewData(data.getLists());
            } else {
                indexBookAdapter.addData(data.getLists());
            }
            if (data.getLists().size() == limit) {
                page++;
                indexBookAdapter.loadMoreComplete();
            } else {
                indexBookAdapter.loadMoreEnd();
            }

        } else {
            indexBookAdapter.loadMoreEnd();
        }

        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh();
        }


    }

    @Override
    public void showEnd() {
        indexBookAdapter.loadMoreEnd();
    }

    @Override
    public void showTintInfo(String tint) {
        stateView.showNoData(smartRefreshLayout, tint);
    }


    public void getData(boolean isRefresh) {

        mPresenter.getCollectList(page, limit, isRefresh);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.COLLECT)
            })
    public void reloadData(String flag) {
        page = 1;
        getData(true);

    }


    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(smartRefreshLayout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(false);
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(smartRefreshLayout);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(smartRefreshLayout);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.LOGIN_SUCCESS)
            })
    public void reloadData(UserInfo userInfo) {
        getData(false);
    }


    private void initRefresh() {
        //设置 Header 为 BezierRadar 样式
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.autoRefresh();
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getData(true);

            }
        });
    }
}

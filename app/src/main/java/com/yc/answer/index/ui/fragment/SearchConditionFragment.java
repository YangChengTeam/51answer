package com.yc.answer.index.ui.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.answer.R;
import com.yc.answer.base.StateView;
import com.yc.answer.index.contract.BookConditionContract;
import com.yc.answer.index.model.bean.BookInfo;
import com.yc.answer.index.presenter.BookConditionPresenter;
import com.yc.answer.index.ui.activity.SearchActivity;
import com.yc.answer.index.ui.adapter.SearchItemAdapter;
import com.yc.answer.index.ui.widget.MyDecoration;
import com.yc.answer.utils.SearchHistoryHelper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseFragment;

/**
 * Created by wanglin  on 2018/3/10 10:09.
 */

public class SearchConditionFragment extends BaseFragment<BookConditionPresenter> implements BookConditionContract.View {

    @BindView(R.id.hot_recyclerView)
    RecyclerView hotRecyclerView;
    @BindView(R.id.history_recyclerView)
    RecyclerView historyRecyclerView;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;


    private SearchItemAdapter hotItemAdapter;
    private SearchItemAdapter historyItemAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_condition;
    }


    @Override
    public void init() {

        mPresenter = new BookConditionPresenter(getActivity(), this);


        initAdapter();

        initListener();
    }

    private void initListener() {
        hotItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                String data = (String) adapter.getItem(position);
                SearchHistoryHelper.saveHistoryList(data);
                ((SearchActivity) getActivity()).replaceFragment(1, data, "", "", "", "", "");
            }
        });

        historyItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String data = (String) adapter.getItem(position);
                SearchHistoryHelper.saveHistoryList(data);
                ((SearchActivity) getActivity()).replaceFragment(1, data, "", "", "", "", "");
            }
        });

        RxView.clicks(tvDelete).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                SearchHistoryHelper.setHistoryList(null);
                setHistoryList();
            }
        });

    }

    private void initAdapter() {
        FlexboxLayoutManager hotLayoutManager = new FlexboxLayoutManager(getActivity());
        hotLayoutManager.setFlexWrap(FlexWrap.WRAP);
        hotLayoutManager.setAlignItems(AlignItems.STRETCH);
        hotRecyclerView.setLayoutManager(hotLayoutManager);
        hotItemAdapter = new SearchItemAdapter(null, true);
        hotRecyclerView.setAdapter(hotItemAdapter);
        hotRecyclerView.addItemDecoration(new MyDecoration(10));


        FlexboxLayoutManager historyLayoutManager = new FlexboxLayoutManager(getActivity());
        historyLayoutManager.setFlexWrap(FlexWrap.WRAP);
        historyLayoutManager.setAlignItems(AlignItems.STRETCH);
        historyRecyclerView.setLayoutManager(historyLayoutManager);


        historyItemAdapter = new SearchItemAdapter(null, false);
        historyRecyclerView.setAdapter(historyItemAdapter);
        historyRecyclerView.addItemDecoration(new MyDecoration(10));
        setHistoryList();
    }

    @Override
    public void showConditionList(List<String> data) {
        hotItemAdapter.setNewData(data);
    }

    @Override
    public void showBookInfoList(List<BookInfo> lists) {

    }

    @Override
    public void showFavoriteResult(boolean isCollect) {

    }


    private void setHistoryList() {
        historyItemAdapter.setNewData(SearchHistoryHelper.getHistoryList());
    }


    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(llContainer, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loadData(true);
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(llContainer);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(llContainer);
    }


}

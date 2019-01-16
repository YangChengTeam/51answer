package com.yc.answer.index.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.answer.R;
import com.yc.answer.base.StateView;
import com.yc.answer.index.contract.BookContract;
import com.yc.answer.index.model.bean.BookInfo;
import com.yc.answer.index.model.bean.BookInfoWrapper;
import com.yc.answer.index.presenter.BookPresenter;
import com.yc.answer.index.ui.activity.AnswerDetailActivity;
import com.yc.answer.index.ui.adapter.SearchResultItemAdapter;
import com.yc.answer.index.ui.widget.FilterPopwindow;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.functions.Action1;
import yc.com.base.BaseFragment;

/**
 * Created by wanglin  on 2018/3/10 10:09.
 */

public class SearchResultFragment extends BaseFragment<BookPresenter> implements BookContract.View {


    @BindView(R.id.result_recyclerView)
    RecyclerView resultRecyclerView;
    @BindView(R.id.tv_sum_count)
    TextView tvSumCount;
    @BindView(R.id.tv_filter)
    TextView tvFilter;
    @BindView(R.id.rl_filter)
    RelativeLayout rlFilter;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;


    private String name;
    private String subject;
    private String grade;
    private String part;
    private String version;
    private int page = 1;
    private int limit = 20;
    private SearchResultItemAdapter itemAdapter;
    private String code;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_result;
    }

    @Override
    public void init() {
        mPresenter = new BookPresenter(getActivity(), this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            name = bundle.getString("text");

            subject = bundle.getString("subject");
            grade = bundle.getString("grade");
            part = bundle.getString("part");
            code = bundle.getString("code");
            version = bundle.getString("version");
        }

        getData();
        initAdapter();
        initListener();
    }

    private void initListener() {
        itemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData();
            }
        }, resultRecyclerView);

        itemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BookInfo bookInfo = (BookInfo) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), AnswerDetailActivity.class);
                intent.putExtra("bookId", bookInfo.getBookId());
                intent.putExtra("bookName", bookInfo.getName());
                startActivity(intent);
            }
        });


        RxView.clicks(tvFilter).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                FilterPopwindow filterPopwindow = new FilterPopwindow(getActivity());
                filterPopwindow.showAsDropDown(rlFilter);
            }
        });

    }

    private void initAdapter() {
        resultRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemAdapter = new SearchResultItemAdapter(null);
        resultRecyclerView.setAdapter(itemAdapter);

    }


    private void getData() {
        mPresenter.getBookList(page, limit, name, code, "", grade, "", part, "", version, "", subject, "", "", "", "");
    }


    @Override
    public void showBookList(BookInfoWrapper data) {
        if (data != null && data.getLists() != null) {
            int count = data.getCount();
            String str = "共找到 <font color=\"#FF0000\"> " + count + "</font> 本";

            tvSumCount.setText(Html.fromHtml(str));
            List<BookInfo> lists = data.getLists();

            if (page == 1) {
                itemAdapter.setNewData(lists);
            } else {
                itemAdapter.addData(lists);
            }

            if (lists.size() > 0 && lists.size() == limit) {
                page++;
                itemAdapter.loadMoreComplete();
            } else {
                itemAdapter.loadMoreEnd();
            }
        } else {
            itemAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showEnd() {
        itemAdapter.loadMoreEnd();
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
                getData();
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(llContainer, "书本和答案正在上架中,请先看看别的书");
    }

    @Override
    public void showLoading() {
        stateView.showLoading(llContainer);
    }


}

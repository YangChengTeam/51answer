package com.yc.answer.index.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.answer.R;
import com.yc.answer.base.StateView;
import com.yc.answer.index.contract.BookContract;
import com.yc.answer.index.model.bean.BookInfo;
import com.yc.answer.index.model.bean.BookInfoWrapper;
import com.yc.answer.index.presenter.BookPresenter;
import com.yc.answer.index.ui.activity.AnswerDetailActivity;
import com.yc.answer.index.ui.adapter.IndexBookAdapter;
import com.yc.answer.index.ui.widget.MyDecoration;

import butterknife.BindView;
import yc.com.base.BaseFragment;


/**
 * Created by wanglin  on 2018/3/7 18:43.
 */

public class IndexBookFragment extends BaseFragment<BookPresenter> implements BookContract.View {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.stateView)
    StateView stateView;


    private String sujectId = "";

    private int page = 1;
    private int limit = 20;
    private IndexBookAdapter indexBookAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.frament_index_book;
    }

    @Override
    public void init() {
        mPresenter = new BookPresenter(getActivity(), this);

        getData();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        indexBookAdapter = new IndexBookAdapter(null);
        recyclerView.setAdapter(indexBookAdapter);
        recyclerView.addItemDecoration(new MyDecoration(20));

        initListener();
    }

    private void initListener() {
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

        indexBookAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData();
            }
        }, recyclerView);
    }


    public void setSujectId(String sujectId) {
        this.sujectId = sujectId;
    }

    @Override
    public void showBookList(BookInfoWrapper data) {
        if (data != null && data.getLists() != null) {
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
    }

    @Override
    public void showEnd() {
        indexBookAdapter.loadMoreEnd();
    }

    private void getData() {
        mPresenter.getBookList(page, limit, "", "", "", "", "", "", "", "", sujectId, "", "", "", "", "");

    }

    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(recyclerView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(recyclerView);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(recyclerView);
    }


}

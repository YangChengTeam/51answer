package com.yc.answer.index.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.answer.R;
import com.yc.answer.base.StateView;
import com.yc.answer.index.contract.UploadBookListContract;
import com.yc.answer.index.model.bean.BookInfo;
import com.yc.answer.index.presenter.UploadBookListPresenter;
import com.yc.answer.index.ui.adapter.UploadBookListAdapter;
import com.yc.answer.index.ui.widget.MyDecoration;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/4/23 12:39.
 */

public class UploadBookListActivity extends BaseActivity<UploadBookListPresenter> implements UploadBookListContract.View {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;
    @BindView(R.id.tv_ok)
    TextView tvOk;
    @BindView(R.id.upload_list_recyclerView)
    RecyclerView uploadListRecyclerView;
    @BindView(R.id.tv_upload)
    TextView tvUpload;

    @BindView(R.id.stateView)
    StateView stateView;
    private UploadBookListAdapter bookListAdapter;
    private int page = 1;
    private static final int LIMIT = 20;

    @Override
    public int getLayoutId() {
        return R.layout.activity_upload_book_list;
    }

    @Override
    public void init() {
        commonTvTitle.setText(getString(R.string.all));
        tvOk.setText(getString(R.string.forward_explain));
        tvOk.setVisibility(View.VISIBLE);
        mPresenter = new UploadBookListPresenter(this, this);
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        uploadListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookListAdapter = new UploadBookListAdapter(null);
        uploadListRecyclerView.setAdapter(bookListAdapter);
        uploadListRecyclerView.addItemDecoration(new MyDecoration(10));
        RxView.clicks(tvOk).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(UploadBookListActivity.this, ForwardActivity.class);
                startActivity(intent);
            }
        });

        RxView.clicks(tvUpload).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(UploadBookListActivity.this, UploadBookActivity.class);
                startActivity(intent);
            }
        });
        bookListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData();
            }
        }, uploadListRecyclerView);
        getData();
    }


    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(uploadListRecyclerView, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(uploadListRecyclerView, "你还没有上传过书籍哦，快来上传得Q币了");
    }

    @Override
    public void showLoading() {
        stateView.showLoading(uploadListRecyclerView);
    }

    @Override
    public void showUploadBookList(List<BookInfo> data) {
        if (page == 1) {
            bookListAdapter.setNewData(data);
        } else {
            bookListAdapter.addData(data);
        }
        if (data.size() == LIMIT) {
            page++;
            bookListAdapter.loadMoreComplete();
        } else {
            bookListAdapter.loadMoreEnd();
        }
    }

    public void getData() {
        mPresenter.getUploadBookList(page, LIMIT);
    }
}

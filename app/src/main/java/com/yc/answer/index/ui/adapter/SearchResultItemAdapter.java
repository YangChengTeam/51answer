package com.yc.answer.index.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.answer.R;
import com.yc.answer.index.model.bean.BookInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/3/10 10:56.
 */

public class SearchResultItemAdapter extends BaseQuickAdapter<BookInfo, BaseViewHolder> {
    public SearchResultItemAdapter(@Nullable List<BookInfo> data) {
        super(R.layout.fragment_search_result_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BookInfo item) {
        helper.setText(R.id.tv_book_title, item.getName()).setText(R.id.tv_author, item.getAuthor())
                .setText(R.id.tv_time, item.getTime());

        Glide.with(mContext).load(item.getCover_img()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)
                .skipMemoryCache(true).error(R.mipmap.ic_launcher).dontAnimate()).thumbnail(0.1f).into((ImageView) helper.getView(R.id.iv_book));

        RecyclerView recyclerView = helper.getView(R.id.tag_recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        recyclerView.setAdapter(new DetailFlagAdapter(item.getFlag(), false));
    }
}

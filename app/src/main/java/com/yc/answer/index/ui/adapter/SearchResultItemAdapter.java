package com.yc.answer.index.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kk.utils.LogUtil;
import com.yc.answer.R;
import com.yc.answer.index.model.bean.BookInfo;
import com.yc.answer.index.model.bean.VersionDetailInfo;
import com.yc.answer.index.ui.widget.MyRecyclerView;

import java.util.ArrayList;
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
                .skipMemoryCache(true).error(R.mipmap.small_placeholder).dontAnimate()).thumbnail(0.1f).into((ImageView) helper.getView(R.id.iv_book));


        List<VersionDetailInfo> flag = item.getFlag();
        if (flag == null || flag.size() == 0) {
            flag = new ArrayList<>();
            flag.add(new VersionDetailInfo("", item.getVersion()));
        }

        MyRecyclerView recyclerView = helper.getView(R.id.tag_recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        DetailFlagAdapter detailFlagAdapter = new DetailFlagAdapter(flag, false);
        recyclerView.setAdapter(detailFlagAdapter);

    }

}

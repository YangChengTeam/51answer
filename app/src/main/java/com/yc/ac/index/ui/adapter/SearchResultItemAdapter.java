package com.yc.ac.index.ui.adapter;

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
import com.yc.ac.R;
import com.yc.ac.index.model.bean.BookInfo;
import com.yc.ac.index.model.bean.VersionDetailInfo;
import com.yc.ac.index.ui.widget.MyRecyclerView;
import com.yc.ac.utils.SubjectHelper;

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
        helper.setText(R.id.tv_book_title, item.getName()).setText(R.id.tv_grade, item.getGrade())
                .setText(R.id.tv_part, item.getPart_type()).setText(R.id.tv_version, item.getVersion()).addOnClickListener(R.id.tv_collect);
        helper.setBackgroundRes(R.id.tv_collect, item.getFavorite() == 1 ? R.drawable.book_collect_gray_bg : R.drawable.book_collect_red_bg);
        helper.setText(R.id.tv_collect, item.getFavorite() == 1 ? "已收藏" : "收藏");

        Glide.with(mContext).load(item.getCover_img()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)
                .skipMemoryCache(true).centerCrop().error(R.mipmap.small_placeholder).dontAnimate()).thumbnail(0.1f).into((ImageView) helper.getView(R.id.iv_book));

        SubjectHelper.setSubject(helper, item, R.id.iv_subject);


//        List<VersionDetailInfo> flag = item.getFlag();
//        if (flag == null || flag.size() == 0) {
//            flag = new ArrayList<>();
//            flag.add(new VersionDetailInfo("", item.getVersion()));
//        }
//
//        MyRecyclerView recyclerView = helper.getView(R.id.tag_recyclerView);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
//
//        DetailFlagAdapter detailFlagAdapter = new DetailFlagAdapter(flag, false);
//        recyclerView.setAdapter(detailFlagAdapter);

    }

}

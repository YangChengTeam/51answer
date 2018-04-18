package com.yc.answer.index.ui.adapter;

import android.support.annotation.Nullable;
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
 * Created by wanglin  on 2018/3/7 19:29.
 */

public class IndexBookAdapter extends BaseQuickAdapter<BookInfo, BaseViewHolder> {
    public IndexBookAdapter(@Nullable List<BookInfo> data) {
        super(R.layout.fragment_book_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BookInfo item) {
        helper.setText(R.id.tv_book_name, item.getName());
        Glide.with(mContext).load(item.getCover_img()).apply(new RequestOptions()
                .placeholder(R.mipmap.small_placeholder).error(R.mipmap.small_placeholder).centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.DATA).skipMemoryCache(true)).into((ImageView) helper.getView(R.id.iv_book));
    }
}

package com.yc.ac.index.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.ac.R;
import com.yc.ac.index.model.bean.BookInfo;
import com.yc.ac.utils.GlideHelper;
import com.yc.ac.utils.SubjectHelper;

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
        helper.setText(R.id.tv_book_name, "        " + item.getName());
        GlideHelper.loadImage(mContext, item.getCover_img(), (ImageView) helper.getView(R.id.iv_book));

        SubjectHelper.setSubject(helper, item, R.id.iv_grade_tag);
    }
}

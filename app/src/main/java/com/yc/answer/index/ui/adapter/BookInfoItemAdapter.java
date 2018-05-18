package com.yc.answer.index.ui.adapter;

import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.answer.R;
import com.yc.answer.index.model.bean.VersionDetailInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/4/20 18:09.
 */

public class BookInfoItemAdapter extends BaseQuickAdapter<VersionDetailInfo, BaseViewHolder> {

    public BookInfoItemAdapter(@Nullable List<VersionDetailInfo> data) {
        super(R.layout.book_info_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VersionDetailInfo item) {
        helper.setText(R.id.tv_item_detail, item.getName());

    }

}

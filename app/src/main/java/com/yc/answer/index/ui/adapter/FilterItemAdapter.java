package com.yc.answer.index.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.answer.R;
import com.yc.answer.index.model.bean.FilterInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/3/8 16:14.
 */

public class FilterItemAdapter extends BaseQuickAdapter<FilterInfo, BaseViewHolder> {
    public FilterItemAdapter(@Nullable List<FilterInfo> data) {
        super(R.layout.popwindow_filter_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FilterInfo item) {
        helper.setText(R.id.tv_title,item.getTitle());
        RecyclerView recyclerView = helper.getView(R.id.item_recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(mContext,4));
        recyclerView.setAdapter(new FilterItemDetailAdapter(item.getList()));

    }
}

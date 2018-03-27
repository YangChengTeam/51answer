package com.yc.answer.index.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.answer.R;
import com.yc.answer.index.contract.IndexContract;
import com.yc.answer.index.model.bean.VersionDetailInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/3/8 16:21.
 */

public class FilterItemDetailAdapter extends BaseQuickAdapter<VersionDetailInfo, BaseViewHolder> {

    private SparseArray<TextView> sparseArray;

    public FilterItemDetailAdapter(@Nullable List<VersionDetailInfo> data) {
        super(R.layout.popwindow_filter_item_detail, data);
        sparseArray = new SparseArray<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, VersionDetailInfo item) {
        helper.setText(R.id.tv_item_detail, item.getName());
        sparseArray.put(helper.getAdapterPosition(), (TextView) helper.getView(R.id.tv_item_detail));
        sparseArray.get(0).setSelected(true);
    }

    public TextView getView(int position) {

        for (int i = 0; i < sparseArray.size(); i++) {
            TextView textView = sparseArray.get(i);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            textView.setSelected(false);
        }
        return sparseArray.get(position);
    }

    public void onClick(int position) {

        TextView textView = getView(position);
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        textView.setSelected(true);

    }
}

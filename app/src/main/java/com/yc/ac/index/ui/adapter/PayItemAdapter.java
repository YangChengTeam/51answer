package com.yc.ac.index.ui.adapter;

import android.graphics.Paint;
import android.graphics.Point;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.ac.R;
import com.yc.ac.index.model.bean.PayItemInfo;

import java.util.List;

public class PayItemAdapter extends BaseQuickAdapter<PayItemInfo, BaseViewHolder> {

    private SparseArray<View> itemViewList;
    private SparseArray<View> textViewList;
    private SparseArray<View> imageViewList;

    public PayItemAdapter(@Nullable List<PayItemInfo> data) {
        super(R.layout.view_pay_item, data);
        itemViewList = new SparseArray<>();
        textViewList = new SparseArray<>();
        imageViewList = new SparseArray<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, PayItemInfo item) {
        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_price, "现价:" + item.getPrice() + "元")
                .setText(R.id.tv_desc, item.getDesc())
                .setText(R.id.tv_origin_price, "原价" + item.getOrigin_price() + "元");
        ((TextView) helper.getView(R.id.tv_origin_price)).setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//设置中划线并设置清晰
        helper.setGone(R.id.tv_tag, item.getIsHot() == 1);
        int adapterPosition = helper.getAdapterPosition();
        if (adapterPosition == 0) {
            helper.itemView.setSelected(true);
            helper.setGone(R.id.iv_sel, true);
        }

        itemViewList.put(adapterPosition, helper.itemView);
        textViewList.put(adapterPosition, helper.getView(R.id.tv_price));
        imageViewList.put(adapterPosition, helper.getView(R.id.iv_sel));

    }

    private void resetView() {
        for (int i = 0; i < itemViewList.size(); i++) {
            itemViewList.get(i).setSelected(false);
            textViewList.get(i).setSelected(false);
            imageViewList.get(i).setVisibility(View.GONE);

        }
    }

    public void setSelect(int position) {
        resetView();
        itemViewList.get(position).setSelected(true);
        textViewList.get(position).setSelected(true);
        imageViewList.get(position).setVisibility(View.VISIBLE);
    }
}

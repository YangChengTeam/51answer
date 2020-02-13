package com.yc.ac.index.ui.adapter;


import android.util.SparseArray;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vondear.rxtools.RxSPTool;
import com.yc.ac.R;
import com.yc.ac.index.model.bean.VersionDetailInfo;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * Created by wanglin  on 2018/3/8 16:21.
 */

public class FilterItemDetailAdapter extends BaseQuickAdapter<VersionDetailInfo, BaseViewHolder> {

    private SparseArray<TextView> sparseArray;
    private String mFlag;

    public FilterItemDetailAdapter(@Nullable List<VersionDetailInfo> data, String flag) {
        super(R.layout.popwindow_filter_item_detail, data);
        this.mFlag = flag;
        sparseArray = new SparseArray<>();

    }

    @Override
    protected void convert(BaseViewHolder helper, VersionDetailInfo item) {
        helper.setText(R.id.tv_item_detail, item.getName());
        sparseArray.put(helper.getLayoutPosition(), (TextView) helper.getView(R.id.tv_item_detail));
        setSelectState(helper);

    }


    private void setSelectState( BaseViewHolder helper) {
        String saveData = RxSPTool.getString(mContext, mFlag);
        int currentPos = 0;
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getName().equals(saveData)) {
                currentPos = i;
                break;
            }
        }
        if (helper.getLayoutPosition() == currentPos) {
            helper.getView(R.id.tv_item_detail).setSelected(true);
        }
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
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.red_f0333a));
        textView.setSelected(true);

    }
}

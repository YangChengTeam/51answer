package com.yc.answer.index.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vondear.rxtools.RxSPTool;
import com.yc.answer.R;
import com.yc.answer.constant.SpConstant;
import com.yc.answer.index.model.bean.VersionDetailInfo;

import java.util.List;

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

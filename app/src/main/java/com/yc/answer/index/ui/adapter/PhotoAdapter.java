package com.yc.answer.index.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.answer.R;


import java.util.List;

/**
 * Created by wanglin  on 2018/1/26 10:41.
 */

public class PhotoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public PhotoAdapter(List<String> data) {
        super(R.layout.dialog_photo_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_title, item);
        int position = helper.getAdapterPosition();
        if (position == mData.size() - 1) {
            helper.setVisible(R.id.divider, false);
        }

    }
}

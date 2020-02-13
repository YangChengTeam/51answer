package com.yc.ac.index.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.ac.R;
import com.yc.ac.index.model.bean.TagInfo;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by wanglin  on 2019/3/14 17:31.
 */
public class IndexTagAdapter extends BaseQuickAdapter<TagInfo, BaseViewHolder> {
    public IndexTagAdapter(@Nullable List<TagInfo> data) {
        super(R.layout.item_info_tag, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TagInfo item) {
        helper.setText(R.id.tv_title, item.getTitle());
    }
}

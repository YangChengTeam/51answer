package com.yc.ac.index.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.ac.R;
import com.yc.ac.index.model.bean.TagInfo;
import com.yc.ac.utils.GlideHelper;

import java.util.List;

/**
 * Created by wanglin  on 2019/3/15 18:14.
 */
public class IndexZtAdapter extends BaseQuickAdapter<TagInfo, BaseViewHolder> {
    public IndexZtAdapter(@Nullable List<TagInfo> data) {
        super(R.layout.index_zt_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TagInfo item) {
        helper.setText(R.id.tv_zt, item.getZtname());
        GlideHelper.loadImage(mContext, item.getZtimg(), (ImageView) helper.getView(R.id.iv_zt));
    }
}

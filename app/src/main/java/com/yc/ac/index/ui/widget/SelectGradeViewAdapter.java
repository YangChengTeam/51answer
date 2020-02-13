package com.yc.ac.index.ui.widget;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.ac.R;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by wanglin  on 2018/4/18 13:55.
 */

public class SelectGradeViewAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SelectGradeViewAdapter(@Nullable List<String> data) {
        super(R.layout.select_grade_view_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_grade_item, item);
    }


}

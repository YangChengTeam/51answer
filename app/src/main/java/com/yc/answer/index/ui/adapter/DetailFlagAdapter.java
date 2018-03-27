package com.yc.answer.index.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.answer.R;
import com.yc.answer.index.model.bean.VersionDetailInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/3/9 20:21.
 */

public class DetailFlagAdapter extends BaseQuickAdapter<VersionDetailInfo, BaseViewHolder> {
    private boolean mIsHot;

    public DetailFlagAdapter(@Nullable List<VersionDetailInfo> data, boolean isHot) {
        super(R.layout.search_item_view, data);
        this.mIsHot = isHot;
    }

    @Override
    protected void convert(BaseViewHolder helper, VersionDetailInfo item) {
        TextView view = helper.getView(R.id.tv_search_item);
        view.setText(item.getName());
        if (mIsHot) {
            int position = helper.getAdapterPosition();
            switch (position % 5) {
                case 0:
                    view.setBackgroundResource(R.drawable.search_item_first_bg);
                    break;
                case 1:

                    view.setBackgroundResource(R.drawable.search_item_second_bg);
                    break;
                case 2:

                    view.setBackgroundResource(R.drawable.search_item_third_bg);
                    break;
                case 3:

                    view.setBackgroundResource(R.drawable.search_item_four_bg);
                    break;
                case 4:
                    view.setBackgroundResource(R.drawable.search_item_five_bg);
                    break;
            }

        }
    }
}

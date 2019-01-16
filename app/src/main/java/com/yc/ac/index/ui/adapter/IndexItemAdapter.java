package com.yc.ac.index.ui.adapter;

import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vondear.rxtools.RxDeviceTool;
import com.yc.ac.R;

import java.util.List;

/**
 * Created by wanglin  on 2018/3/9 20:21.
 */

public class IndexItemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private boolean mIsHot;

    public IndexItemAdapter(@Nullable List<String> data, boolean isHot) {
        super(R.layout.index_item_view, data);
        this.mIsHot = isHot;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {


        TextView view = helper.getView(R.id.tv_search_item);
        view.setText(item);
        if (mIsHot) {
            int position = helper.getAdapterPosition();
            switch (position % 5) {
                case 0:
                    view.setBackgroundResource(R.drawable.search_item_first_rect_bg);
                    break;
                case 1:

                    view.setBackgroundResource(R.drawable.search_item_rect_second_bg);
                    break;
                case 2:

                    view.setBackgroundResource(R.drawable.search_item_rect_third_bg);
                    break;
                case 3:

                    view.setBackgroundResource(R.drawable.search_item_rect_four_bg);
                    break;
                case 4:
                    view.setBackgroundResource(R.drawable.search_item_rect_five_bg);
                    break;
            }

        }
    }
}

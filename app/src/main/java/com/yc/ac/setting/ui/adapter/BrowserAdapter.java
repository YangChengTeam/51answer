package com.yc.ac.setting.ui.adapter;

import android.util.Log;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.ac.R;
import com.yc.ac.setting.model.bean.BrowserInfo;
import com.yc.ac.utils.GlideHelper;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by wanglin  on 2019/3/15 10:51.
 */
public class BrowserAdapter extends BaseQuickAdapter<BrowserInfo, BaseViewHolder> {


    public BrowserAdapter(@Nullable List<BrowserInfo> data) {
        super(R.layout.browser_item, data);

//        setHasStableIds(true);
    }

    @Override
    protected void convert(BaseViewHolder helper, BrowserInfo item) {
        helper.setText(R.id.tv_book_title, item.getName())
                .setText(R.id.tv_last_page, String.format(mContext.getString(R.string.last_browser_page), item.getLastPage()))
                .setText(R.id.tv_browser_date, item.getBrowserTime())
                .setGone(R.id.tv_browser_date, item.isShow())
                .addOnClickListener(R.id.iv_select)
                .setGone(R.id.iv_select, item.isShowIcon());

//        Log.e("TAG", "img:" + item.getCover_img());
        GlideHelper.loadImage(mContext, item.getCover_img(), helper.getView(R.id.iv_book));

    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).getId();
    }
}

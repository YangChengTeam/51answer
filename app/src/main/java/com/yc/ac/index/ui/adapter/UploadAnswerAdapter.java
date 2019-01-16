package com.yc.ac.index.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.ac.R;
import com.yc.ac.setting.model.bean.UploadInfo;
import com.yc.ac.utils.GlideHelper;

import java.util.List;

/**
 * Created by wanglin  on 2018/4/20 15:16.
 */

public class UploadAnswerAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public UploadAnswerAdapter(List<String> data) {
        super(R.layout.upload_answer_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.addOnClickListener(R.id.iv_delete_answer);
        int position = helper.getLayoutPosition();
        if (position == getData().size() - 1) {
            helper.setImageResource(R.id.iv_answer, R.mipmap.add_answer);
            helper.setVisible(R.id.iv_delete_answer, false);
        } else {
            GlideHelper.loadImage(mContext, item, (ImageView) helper.getView(R.id.iv_answer));
            helper.setVisible(R.id.iv_delete_answer, true);
        }
    }


}

package com.yc.ac.index.ui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.ac.R;
import com.yc.ac.index.model.bean.BookInfo;
import com.yc.ac.utils.GlideHelper;
import com.yc.ac.utils.SubjectHelper;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by wanglin  on 2018/4/23 12:47.
 */

public class UploadBookListAdapter extends BaseQuickAdapter<BookInfo, BaseViewHolder> {
    public UploadBookListAdapter(@Nullable List<BookInfo> data) {
        super(R.layout.activity_upload_book_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BookInfo item) {
        helper.setText(R.id.tv_book_title, item.getName()).setText(R.id.tv_grade, item.getGrade())
                .setText(R.id.tv_part, item.getPart_type()).setText(R.id.tv_version, item.getVersion())
                .setText(R.id.tv_time, item.getTime());
        SubjectHelper.setSubject(helper, item, R.id.iv_subject);
        GlideHelper.loadImage(mContext, item.getCover_img(), (ImageView) helper.getView(R.id.iv_book));
        helper.setImageResource(R.id.iv_audit_state, item.getState() == 0 ? R.mipmap.pass : item.getState() == 1 ? R.mipmap.reject : R.mipmap.pass);
    }
}

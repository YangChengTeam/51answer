package com.yc.answer.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yc.answer.R;

import butterknife.BindView;
import yc.com.base.BaseView;

/**
 * Created by wanglin  on 2018/3/21 17:27.
 */

public class BaseBookView extends BaseView {
    @BindView(R.id.iv_book)
    ImageView ivBook;
    @BindView(R.id.tv_book_name)
    TextView tvBookName;

    public BaseBookView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_book_item;
    }


    public void setBookInfo(String url, String name) {
        Glide.with(mContext).load(url).apply(new RequestOptions().error(R.mipmap.big_placeholder).centerCrop().placeholder(R.mipmap.big_placeholder)).into(ivBook);
        tvBookName.setText(name);
    }
}

package com.yc.ac.index.ui.activity;

import com.github.chrisbanes.photoview.PhotoView;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.ac.R;
import com.yc.ac.utils.GlideHelper;

import java.util.concurrent.TimeUnit;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/4/23 10:52.
 */

public class AnswerPreviewActivity extends BaseActivity {
    @BindView(R.id.xImageView)
    PhotoView xImageView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_answer_detail_item;
    }

    @Override
    public void init() {
        xImageView.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
        String path = getIntent().getStringExtra("path");
        GlideHelper.loadImage(this, path, xImageView, R.mipmap.big_placeholder, false);
        RxView.clicks(xImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
    }


}

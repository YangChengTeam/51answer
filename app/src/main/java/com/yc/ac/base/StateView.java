package com.yc.ac.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jakewharton.rxbinding.view.RxView;
import com.vondear.rxtools.RxImageTool;
import com.yc.ac.R;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseView;


/**
 * Created by wanglin  on 2018/2/27 16:13.
 */

public class StateView extends BaseView {
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.tv_mess)
    TextView tvMess;
    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;
    private View mContentView;

    public StateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StateView);
        try {
            int gravity = ta.getInt(R.styleable.StateView_gravity, 5);
            setGravity(gravity);

        } finally {
            ta.recycle();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_state_view;
    }


    @Override
    public void init() {

    }

    public void showLoading(View contentView, String mess) {
        mContentView = contentView;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivLoading.getLayoutParams();

        layoutParams.width = RxImageTool.dp2px(1080f / 3);
        layoutParams.height = RxImageTool.dp2px(600f / 3);
        ivLoading.setLayoutParams(layoutParams);
        setVisibility(VISIBLE);
        tvMess.setText(mess);
        ivLoading.setVisibility(VISIBLE);
        RequestOptions requestOptions = RequestOptions.overrideOf(RxImageTool.dp2px(1080f / 3), RxImageTool.dp2px(600f / 3));

        Glide.with(this).load(R.mipmap.loading).apply(requestOptions).into(ivLoading);

        mContentView.setVisibility(GONE);


    }


    public void showLoading(View contentView) {
        showLoading(contentView, "正在加载中，请稍候...");

    }


    public void showNoData(View contentView, CharSequence message, final OnClickListener listener) {
        mContentView = contentView;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivLoading.getLayoutParams();
        layoutParams.width = RxImageTool.dp2px(344f / 3);
        layoutParams.height = RxImageTool.dp2px(276f / 3);
        ivLoading.setLayoutParams(layoutParams);
        setVisibility(VISIBLE);
        tvMess.setText(message);
        mContentView.setVisibility(GONE);
        ivLoading.setVisibility(GONE);
        RxView.clicks(rlContainer).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (listener != null) listener.onClick(rlContainer);
            }
        });

    }

    public void showNoData(View contentView, String message) {
        showNoData(contentView, message, null);

    }

    public void showNoData(View contentView) {
        showNoData(contentView, "没有发现相关书本和答案");
    }


    public void showNoNet(View contentView, String message, final OnClickListener onClickListener) {

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivLoading.getLayoutParams();
        layoutParams.width = RxImageTool.dp2px(256f / 3);
        layoutParams.height = RxImageTool.dp2px(256f / 3);
        ivLoading.setLayoutParams(layoutParams);
        mContentView = contentView;
        setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.GONE);
        tvMess.setText(message);
        ivLoading.setVisibility(GONE);
        RequestOptions requestOptions = RequestOptions.overrideOf(RxImageTool.dp2px(256f / 3), RxImageTool.dp2px(256f / 3));

//        Glide.with(this).load(R.mipmap.base_no_wifi).apply(requestOptions).into(ivLoading);


        RxView.clicks(rlContainer).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (onClickListener != null)
                    onClickListener.onClick(rlContainer);
            }
        });
    }

    public void hide() {
        setVisibility(GONE);
        if (mContentView != null)
            mContentView.setVisibility(VISIBLE);
    }

    public void showNoNet(View contentView, final OnClickListener onClickListener) {
        showNoNet(contentView, "加载失败，点击重试", onClickListener);
    }

    public void setGravity(int gravity) {
        FrameLayout.LayoutParams layoutParams = (LayoutParams) rlContainer.getLayoutParams();
//            layoutParams.topMargin = SizeUtils.dp2px(30);
//            rlContainer.setLayoutParams(layoutParams);
        switch (gravity) {
            case 1://top
                layoutParams.gravity = Gravity.TOP;
                break;
            case 2://left
                layoutParams.gravity = Gravity.LEFT;
                break;
            case 3://right
                layoutParams.gravity = Gravity.RIGHT;
                break;
            case 4://Bottom
                layoutParams.gravity = Gravity.BOTTOM;
                break;
            case 5://Center
                layoutParams.gravity = Gravity.CENTER;
                break;
        }
        rlContainer.setLayoutParams(layoutParams);
    }
}

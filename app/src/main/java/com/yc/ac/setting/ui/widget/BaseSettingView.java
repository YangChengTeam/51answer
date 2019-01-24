package com.yc.ac.setting.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yc.ac.R;

import butterknife.BindView;
import yc.com.base.BaseView;

/**
 * Created by wanglin  on 2018/3/9 13:32.
 */

public class BaseSettingView extends BaseView {
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right_arrow)
    ImageView ivRightArrow;
    @BindView(R.id.iv_avator)
    ImageView ivAvator;
    @BindView(R.id.tv_extra)
    TextView tvExtra;
    @BindView(R.id.fl_extra)
    FrameLayout flExtra;
    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;
    private Drawable mDrawable;

    private CharSequence mTitle;
    private float mTitleSize;
    private boolean isShowExtra;


    public BaseSettingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseSettingView);

        try {
            mDrawable = ta.getDrawable(R.styleable.BaseSettingView_icon);
            mTitle = ta.getString(R.styleable.BaseSettingView_title);

            mTitleSize = ta.getDimension(R.styleable.BaseSettingView_titleSize, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
            isShowExtra = ta.getBoolean(R.styleable.BaseSettingView_show_extra, false);

            setClickable(true);
            if (mDrawable != null) {
                ivIcon.setImageDrawable(mDrawable);
            }
            if (!TextUtils.isEmpty(mTitle))
                tvTitle.setText(mTitle);
            tvTitle.getPaint().setTextSize(mTitleSize);
            flExtra.setVisibility(isShowExtra ? VISIBLE : GONE);
            ivIcon.setVisibility(isShowExtra ? GONE : VISIBLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rlContainer.setBackgroundResource(R.drawable.btn_ripper_bg);
            } else {
                rlContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            }

        } finally {
            ta.recycle();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_setting_view;
    }

    private String extraText;

    public void setExtraText(String text) {
        tvExtra.setText(text);
        this.extraText = text;
    }

    public void setTitle(CharSequence mTitle) {
        this.mTitle = mTitle;
        tvTitle.setText(mTitle);
    }

    public void setExtraColor(int extraColor) {
        tvExtra.setTextColor(extraColor);
    }

    public String getExtraText() {
        return extraText;
    }

    public void setIvIcon(String path) {
        Glide.with(mContext).load(path).apply(new RequestOptions().error(R.mipmap.default_avatar).circleCrop()).into(ivAvator);
    }

    public void clearIcon() {
        Glide.with(this).clear(ivAvator);
    }


}

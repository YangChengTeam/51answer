package com.yc.answer.setting.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.answer.R;

import butterknife.BindView;
import yc.com.base.BaseView;

/**
 * Created by wanglin  on 2018/5/21 09:35.
 */

public class BaseIncomeView extends BaseView {
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_name)
    TextView tvName;
    private Drawable mDrawable;
    private CharSequence mTitle;
    private float mTitleSize;

    public BaseIncomeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IncomeView);

        try {

            mDrawable = ta.getDrawable(R.styleable.IncomeView_pic);

            mTitle = ta.getString(R.styleable.IncomeView_name);
            mTitleSize = ta.getDimension(R.styleable.IncomeView_nameSize, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14.0f, context.getResources().getDisplayMetrics()));
            if (mDrawable != null) {
                ivIcon.setImageDrawable(mDrawable);
            }
            if (!TextUtils.isEmpty(mTitle)) {
                tvName.setText(mTitle);
            }
            tvName.getPaint().setTextSize(mTitleSize);

        } finally {
            ta.recycle();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_income_view;
    }
}

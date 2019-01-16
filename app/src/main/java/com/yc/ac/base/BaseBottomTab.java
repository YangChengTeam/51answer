package com.yc.ac.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.ac.R;

import butterknife.BindView;
import yc.com.base.BaseView;
import yc.com.base.EmptyUtils;

/**
 * Created by wanglin  on 2018/4/10 15:05.
 */

public class BaseBottomTab extends BaseView {
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_text)
    TextView tvText;

    private Drawable selectIcon;//选中的icon
    private Drawable unSelectIcon;//未选中的icon
    private CharSequence text;//图标文字
    private int selectColor;//选中文字颜色
    private int unSelectColor;//未选中文字颜色
    private boolean isSelect;//是否选中

    public BaseBottomTab(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseBottomTab);

        try {
            selectIcon = ta.getDrawable(R.styleable.BaseBottomTab_select_icon);
            unSelectIcon = ta.getDrawable(R.styleable.BaseBottomTab_unselect_icon);
            text = ta.getString(R.styleable.BaseBottomTab_tab_title);
            selectColor = ta.getColor(R.styleable.BaseBottomTab_select_textColor, getResources().getColor(R.color.red_f14343));
            unSelectColor = ta.getColor(R.styleable.BaseBottomTab_unselect_textColor, getResources().getColor(R.color.gray_bfbfbf));
            isSelect = ta.getBoolean(R.styleable.BaseBottomTab_select, false);
            if (!TextUtils.isEmpty(text)) tvText.setText(text);
            setSelectOrUnSelect(isSelect);
        } finally {
            ta.recycle();
        }


    }

    @Override
    public int getLayoutId() {
        return R.layout.bottom_tab;
    }

    private void setSelectOrUnSelect(boolean flag) {
        if (flag) {
            if (EmptyUtils.isNotEmpty(selectIcon))
                ivIcon.setImageDrawable(selectIcon);
            tvText.setTextColor(selectColor);
        } else {
            if (EmptyUtils.isNotEmpty(unSelectIcon))
                ivIcon.setImageDrawable(unSelectIcon);
            tvText.setTextColor(unSelectColor);
        }
    }


    public void setSelect(boolean select) {
        setSelectOrUnSelect(select);
    }
}

package com.yc.ac.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kk.utils.ScreenUtil;

public class ItemDecorationHelper extends RecyclerView.ItemDecoration {

    private int right;
    private int top;
    private int left;
    private int bottom;



    public ItemDecorationHelper(int bottom) {
        this.bottom = bottom;
    }

    public ItemDecorationHelper(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(ScreenUtil.dip2px(view.getContext(), left), ScreenUtil.dip2px(view.getContext(), top), ScreenUtil.dip2px(view.getContext(), right), ScreenUtil.dip2px(view.getContext(), bottom));
    }
}

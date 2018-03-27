package com.yc.answer.index.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vondear.rxtools.RxImageTool;

/**
 * Created by wanglin  on 2018/3/8 18:43.
 */

public class MyDecoration extends RecyclerView.ItemDecoration {

    private int width;
    private int height;

    public MyDecoration(int height) {
        this(height, 0);
    }

    public MyDecoration(int height, int width) {
        this.height = height;
        this.width = width;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, RxImageTool.dp2px(width), RxImageTool.dip2px(height));
    }
}

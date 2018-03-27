package com.yc.answer.index.ui.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by wanglin  on 2018/3/7 19:08.
 */

public class MyViewPager extends ViewPager {
    private int touchSlop;


    public MyViewPager(@NonNull Context context) {
        super(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int x = 0;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - x) > touchSlop) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
        }

        return false;
    }
}

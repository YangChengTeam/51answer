package com.yc.ac.index.ui.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.kk.utils.LogUtil;

/**
 * Created by wanglin  on 2019/3/17 09:22.
 */
public class CommonNestScrollView extends NestedScrollView {
    private int scrollTouch;
    private int startY;

    public CommonNestScrollView(Context context) {
        super(context);
        scrollTouch = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public CommonNestScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        scrollTouch = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public CommonNestScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scrollTouch = ViewConfiguration.get(context).getScaledTouchSlop();
    }


//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                startY = (int) ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                LogUtil.msg("tag: onInterceptTouchEvent=" + Math.abs(ev.getY() - startY));
//                if (Math.abs(ev.getY() - startY) > 0) {
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                } else {
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                }
//                return true;
//        }
//
//        return super.onInterceptTouchEvent(ev);
//    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.msg("tag: onTouchEvent=" + Math.abs(ev.getY() - startY));
                getParent().requestDisallowInterceptTouchEvent(true);
//                if (Math.abs(ev.getY() - startY) > 0) {
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                } else {
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                }
               break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }

        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollChangeListener != null) {
            onScrollChangeListener.onScroll(l, t, oldl, oldt);
        }
    }

    public onScrollChangeListener onScrollChangeListener;

    public void setOnScrollChangeListener(CommonNestScrollView.onScrollChangeListener onScrollChangeListener) {
        this.onScrollChangeListener = onScrollChangeListener;
    }

    public interface onScrollChangeListener {
        void onScroll(int l, int t, int oldl, int oldt);
    }
}

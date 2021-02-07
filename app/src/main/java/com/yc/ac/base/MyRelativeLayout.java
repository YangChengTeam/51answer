package com.yc.ac.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by suns  on 2021/1/26 18:07.
 */
public class MyRelativeLayout extends RelativeLayout {

    public MyRelativeLayout(Context context) {
        this(context, null);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (isInterceptTouchEvent()) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return isInterceptTouchEvent();
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (listener != null) {
                    listener.onTouchEvent();
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    private boolean isIntercept = false;

    public void setIsInterceptTouchEvent(boolean intercept) {
        this.isIntercept = intercept;
    }

    public boolean isInterceptTouchEvent() {
        return isIntercept;
    }

    private onTouchEventListener listener;

    public void setListener(onTouchEventListener listener) {
        this.listener = listener;
    }

    public interface onTouchEventListener {
        void onTouchEvent();
    }
}

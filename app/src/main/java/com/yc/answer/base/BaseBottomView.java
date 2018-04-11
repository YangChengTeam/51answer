package com.yc.answer.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.yc.answer.R;

import butterknife.BindView;
import butterknife.OnClick;
import yc.com.base.BaseView;

/**
 * Created by wanglin  on 2018/4/10 15:05.
 */

public class BaseBottomView extends BaseView {


    @BindView(R.id.index_tab)
    BaseBottomTab indexTab;
    @BindView(R.id.collect_tab)
    BaseBottomTab collectTab;
    @BindView(R.id.my_tab)
    BaseBottomTab myTab;


    public BaseBottomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        indexTab.setTag(0);
        collectTab.setTag(1);
        myTab.setTag(2);
    }

    @Override
    public int getLayoutId() {
        return R.layout.bottom_view;
    }

    @OnClick({R.id.index_tab, R.id.collect_tab, R.id.my_tab})
    public void onClick(BaseBottomTab tab) {
        if (listener == null) throw new NullPointerException("tabSelectedListener == null");
        clearTabState();
        int idx = (int) tab.getTag();
        listener.onTabSelect(idx);

    }

    public void selectTab(int position) {
        clearTabState();
        switch (position) {
            case 0:
                indexTab.setSelect(true);
                break;
            case 1:
                collectTab.setSelect(true);
                break;
            case 2:
                myTab.setSelect(true);
                break;
        }
    }


    private void clearTabState() {
        indexTab.setSelect(false);
        collectTab.setSelect(false);
        myTab.setSelect(false);
    }

    public void setTabSelectedListener(onTabSelectedListener tabSelectedListener) {
        this.listener = tabSelectedListener;
    }

    private onTabSelectedListener listener;

    public interface onTabSelectedListener {
        void onTabSelect(int position);
    }


}

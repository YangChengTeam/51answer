package com.yc.answer.index.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.answer.R;
import com.yc.answer.utils.ActivityScanHelper;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseView;

/**
 * Created by wanglin  on 2018/3/7 18:21.
 */

public class BaseSearchView extends BaseView {
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.et_search)
    TextView etSearch;
    @BindView(R.id.iv_scan)
    ImageView ivScan;
    @BindView(R.id.btn_search)
    TextView btnSearch;
    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;


    public BaseSearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public int getLayoutId() {
        return R.layout.common_search_view;
    }


    @Override
    public void init() {
        super.init();


        RxView.clicks(ivScan).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {


                ActivityScanHelper.startScanerCode(mContext);

            }
        });
//        setEditTextHintSize(etSearch);

    }

    public void setSearchStyle(boolean flag) {
        if (flag) {
            rlContainer.setBackgroundResource(R.drawable.search_gray_bg);
//            ivScan.setImageResource(R.mipmap.scan_black);
            btnSearch.setBackgroundResource(R.drawable.search_gray_bg);
        } else {
            rlContainer.setBackgroundResource(R.drawable.search_white_bg);
//            ivScan.setImageResource(R.mipmap.scan_gray);
            btnSearch.setBackgroundResource(R.drawable.search_white_bg);
        }
        invalidate();

    }


    public interface onClickListener {
        void onClick();
    }

    private onClickListener onClickListener;

    public void setOnClickListener(BaseSearchView.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}

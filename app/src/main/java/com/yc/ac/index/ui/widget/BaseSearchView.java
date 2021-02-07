package com.yc.ac.index.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.ac.R;
import com.yc.ac.utils.ActivityScanHelper;
import com.yc.ac.utils.ToastUtils;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    EditText etSearch;
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
        RxView.clicks(btnSearch).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String keyWord = etSearch.getText().toString().trim();
                if (TextUtils.isEmpty(keyWord)) {
                    ToastUtils.showCenterToast(getContext(), "搜索内容不能为空");
                    return;
                }
//                etSearch.setFocusable(false);
                if (onClickListener != null) {
                    onClickListener.onClick(keyWord);
                }

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
        void onClick(String keyword);
    }

    private onClickListener onClickListener;

    public void setOnClickListener(BaseSearchView.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}

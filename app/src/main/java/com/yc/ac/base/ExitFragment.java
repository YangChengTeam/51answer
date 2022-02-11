package com.yc.ac.base;

import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.ac.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseDialogFragment;

/**
 * Created by wanglin  on 2018/3/16 18:53.
 */

public class ExitFragment extends BaseDialogFragment {
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_tint)
    TextView tvTint;
    private String mTint;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_exit;
    }

    @Override
    public void init() {
        if (TextUtils.isEmpty(mTint)) {
            mTint = String.format(getString(R.string.is_exit), getString(R.string.app_name));
        }
        tvTint.setText(mTint);
        RxView.clicks(tvConfirm).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (onConfirmListener != null) {
                    onConfirmListener.onConfirm();
                }
            }
        });

        RxView.clicks(tvCancel).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });
    }


    public void setTintContent(String tint) {
        this.mTint = tint;
    }

    @Override
    protected float getWidth() {
        return 0.8f;
    }

    @Override
    public int getAnimationId() {
        return 0;
    }

    @Override
    public int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }


    public interface onConfirmListener {
        void onConfirm();
    }

    public onConfirmListener onConfirmListener;

    public void setOnConfirmListener(ExitFragment.onConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }
}

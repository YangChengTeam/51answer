package com.yc.answer.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.answer.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
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


    @Override
    public int getLayoutId() {
        return R.layout.fragment_exit;
    }

    @Override
    public void init() {
        tvTint.setText(String.format(getString(R.string.is_exit), getString(R.string.app_name)));
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

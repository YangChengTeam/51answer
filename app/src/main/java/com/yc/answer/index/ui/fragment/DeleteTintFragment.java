package com.yc.answer.index.ui.fragment;

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
 * Created by wanglin  on 2018/3/13 16:54.
 */

public class DeleteTintFragment extends BaseDialogFragment {
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_delete_tint;
    }

    @Override
    public void init() {
        RxView.clicks(tvCancel).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //todo分享
                dismiss();
            }
        });
        RxView.clicks(tvConfirm).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (onConfirmListener != null) {
                    onConfirmListener.onConfirm();
                    dismiss();
                }
            }
        });

    }

    @Override
    protected float getWidth() {
        return 0.8f;
    }

    @Override
    public int getAnimationId() {
        return R.style.share_anim;
    }

    @Override
    public int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    public interface onConfirmListener {
        void onConfirm();
    }

    private onConfirmListener onConfirmListener;

    public void setOnConfirmListener(DeleteTintFragment.onConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }
}

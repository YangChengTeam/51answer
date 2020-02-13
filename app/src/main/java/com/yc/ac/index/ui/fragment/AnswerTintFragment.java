package com.yc.ac.index.ui.fragment;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.ac.R;
import com.yc.ac.index.model.bean.BookInfo;
import com.yc.ac.setting.model.bean.ShareInfo;

import com.yc.ac.utils.UserInfoHelper;

import java.util.concurrent.TimeUnit;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseDialogFragment;

/**
 * Created by wanglin  on 2018/3/13 16:54.
 */

public class AnswerTintFragment extends BaseDialogFragment {
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    private BookInfo bookInfo;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_share_tint;
    }

    @Override
    public void init() {

        if (getArguments() != null) {
            bookInfo = getArguments().getParcelable("bookInfo");

        }
        RxView.clicks(tvConfirm).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            //todo分享
            if (!UserInfoHelper.isGoToLogin(getActivity())) {
                ShareFragment shareFragment = new ShareFragment();

                Bundle bundle = new Bundle();
                bundle.putParcelable("bookInfo", bookInfo);

                shareFragment.setArguments(bundle);

                shareFragment.show(getFragmentManager(), null);

//            if (confirmListener != null) {
//                confirmListener.onConfirm();
//            }
            }
            dismiss();
        });
    }

    @Override
    protected float getWidth() {
        return 0.7f;
    }

    @Override
    public int getAnimationId() {
        return R.style.share_anim;
    }

    @Override
    public int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    private OnConfirmListener confirmListener;

    public void setConfirmListener(OnConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
    }

    public interface OnConfirmListener {
        void onConfirm();
    }


}

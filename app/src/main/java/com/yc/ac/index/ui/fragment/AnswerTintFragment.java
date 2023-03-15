package com.yc.ac.index.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.ac.R;
import com.yc.ac.index.model.bean.BookInfo;
import com.yc.ac.index.ui.activity.PayActivity;
import com.yc.ac.utils.UserInfoHelper;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import yc.com.base.BaseDialogFragment;

/**
 * Created by wanglin  on 2018/3/13 16:54.
 */

public class AnswerTintFragment extends BaseDialogFragment {
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.tv_content)
    TextView tvContent;
    private BookInfo bookInfo;
    private int state; // 0 分享 1 支付
    private String articleId;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_share_tint;
    }

    @Override
    public void init() {

        if (getArguments() != null) {
            bookInfo = getArguments().getParcelable("bookInfo");
            state = getArguments().getInt("state");
            articleId = getArguments().getString("articleId");
        }
        if (state == 1) {
            tvContent.setText("支付后即可查看完整答案！！");
        }
        RxView.clicks(tvConfirm).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            //todo分享
            if (!UserInfoHelper.isGoToLogin(getActivity())) {
                if (state == 0) {
                    ShareFragment shareFragment = new ShareFragment();

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("bookInfo", bookInfo);

                    shareFragment.setArguments(bundle);
                    shareFragment.show(getChildFragmentManager(), null);
                } else if (state == 1) {
//                    startActivity(new Intent(requireActivity(), PayActivity.class));
                    PayActivity.startActivity(requireActivity(), articleId);
                }
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

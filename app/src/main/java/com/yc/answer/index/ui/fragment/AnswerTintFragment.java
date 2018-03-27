package com.yc.answer.index.ui.fragment;

import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.answer.R;
import com.yc.answer.setting.model.bean.ShareInfo;
import com.yc.answer.setting.ui.fragment.ShareFragment;
import com.yc.answer.utils.UserInfoHelper;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseDialogFragment;

/**
 * Created by wanglin  on 2018/3/13 16:54.
 */

public class AnswerTintFragment extends BaseDialogFragment {
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    private ShareInfo shareInfo;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_share_tint;
    }

    @Override
    public void init() {

        if (getArguments() != null) {
            shareInfo = getArguments().getParcelable("share");

        }
        RxView.clicks(tvConfirm).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //todo分享
                if (!UserInfoHelper.isGoToLogin(getActivity())) {
                    ShareFragment shareFragment = new ShareFragment();
                    shareFragment.setShareInfo(shareInfo);
                    shareFragment.show(getFragmentManager(), null);
                }
                dismiss();
            }
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




}

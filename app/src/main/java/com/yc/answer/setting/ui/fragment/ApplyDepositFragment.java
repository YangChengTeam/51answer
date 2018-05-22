package com.yc.answer.setting.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.answer.R;
import com.yc.answer.setting.ui.activity.EarningsDetailActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.functions.Action1;
import yc.com.base.BaseDialogFragment;

/**
 * Created by wanglin  on 2018/5/21 11:40.
 */

public class ApplyDepositFragment extends BaseDialogFragment {
    @BindView(R.id.btn_done)
    Button btnDone;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_apply_deposit;
    }

    @Override
    public void init() {
        RxView.clicks(btnDone).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //todo 这里跳到那暂不知道
                startActivity(new Intent(getActivity(), EarningsDetailActivity.class));
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
        return LinearLayout.LayoutParams.WRAP_CONTENT;
    }

}

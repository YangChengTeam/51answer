package com.yc.answer.index.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.vondear.rxtools.RxDeviceTool;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.RxSPTool;
import com.yc.answer.R;
import com.yc.answer.base.MainActivity;
import com.yc.answer.constant.SpConstant;
import com.yc.answer.index.ui.widget.SelectGradeView;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/3/15 14:11.
 */

public class SplashActivity extends BaseActivity {

    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.smallGradeView)
    SelectGradeView smallGradeView;
    @BindView(R.id.middleGradeView)
    SelectGradeView middleGradeView;
    @BindView(R.id.seniorGradeView)
    SelectGradeView seniorGradeView;
    @BindView(R.id.ll_select_grade)
    LinearLayout llSelectGrade;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.rl_select_grade)
    RelativeLayout rlSelectGrade;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void init() {
        if (RxSPTool.getBoolean(this, SpConstant.IS_FIRST_OPEN)) {
            rlSelectGrade.setVisibility(View.GONE);
            iv.setVisibility(View.VISIBLE);
            switchActivity();

        } else {
            RxSPTool.putBoolean(this, SpConstant.IS_FIRST_OPEN, true);
            rlSelectGrade.setVisibility(View.VISIBLE);
            iv.setVisibility(View.GONE);
        }
        initSelectView();
        initListener();
    }

    private void switchActivity() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 500);
    }

    private void initListener() {
        RxView.clicks(tvSkip).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                switchActivity();
            }
        });
    }

    private void initSelectView() {
        if (RxDeviceTool.getScreenHeight(this) >= 1920) {
            RelativeLayout.MarginLayoutParams params = (RelativeLayout.MarginLayoutParams) llSelectGrade.getLayoutParams();
            params.topMargin = RxImageTool.dip2px(RxDeviceTool.getScreenHeight(this) / 10);
            llSelectGrade.setLayoutParams(params);
        }

        smallGradeView.setContents(Arrays.asList(getResources().getStringArray(R.array.small_grade)));
        middleGradeView.setContents(Arrays.asList(getResources().getStringArray(R.array.middle_grade)));
        seniorGradeView.setContents(Arrays.asList(getResources().getStringArray(R.array.senior_grade)));

        setSelectState(smallGradeView);
        setSelectState(middleGradeView);
        setSelectState(seniorGradeView);

    }


    private void setSelectState(final SelectGradeView view) {
        view.setOnSelectGradeListener(new SelectGradeView.OnSelectGradeListener() {
            @Override
            public void onSelect(int position) {
                middleGradeView.clearSelect();
                seniorGradeView.clearSelect();
                smallGradeView.clearSelect();
                view.click(position);
                switchActivity();
            }
        });

    }


}

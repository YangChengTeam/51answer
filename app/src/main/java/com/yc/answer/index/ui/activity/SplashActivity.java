package com.yc.answer.index.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qq.e.ads.nativ.NativeExpressADView;
import com.vondear.rxtools.RxDeviceTool;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.RxSPTool;
import com.yc.answer.R;
import com.yc.answer.base.Constant;
import com.yc.answer.base.MainActivity;
import com.yc.answer.constant.SpConstant;
import com.yc.answer.index.ui.widget.SelectGradeView;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import yc.com.base.BaseActivity;
import yc.com.base.StatusBarUtil;
import yc.com.tencent_adv.AdvDispatchManager;
import yc.com.tencent_adv.AdvType;
import yc.com.tencent_adv.OnAdvStateListener;

/**
 * Created by wanglin  on 2018/3/15 14:11.
 */

public class SplashActivity extends BaseActivity implements OnAdvStateListener {

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
    @BindView(R.id.splash_container)
    FrameLayout splashContainer;
    @BindView(R.id.skip_view)
    TextView skipView;

    private static final int Time = 1000;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void init() {
        if (RxSPTool.getBoolean(this, SpConstant.IS_FIRST_OPEN)) {
            rlSelectGrade.setVisibility(View.GONE);
            iv.setVisibility(View.VISIBLE);
            AdvDispatchManager.getManager().init(this, AdvType.SPLASH, splashContainer, skipView, Constant.TENCENT_ADV_ID, Constant.SPLASH_ADV_ID, this);

        } else {
            RxSPTool.putBoolean(this, SpConstant.IS_FIRST_OPEN, true);
            rlSelectGrade.setVisibility(View.VISIBLE);
            iv.setVisibility(View.GONE);
        }
        initSelectView();

    }

    private void switchActivity(long delay) {

        long delayTime = 0;
        if (delay < Time) {
            delayTime = Time - delay;
        }
        goActivity(delayTime);
    }

    private void goActivity(long delayTime) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);

                startActivity(intent);
                finish();
            }
        }, delayTime);
    }


    private void initSelectView() {
        if (RxDeviceTool.getScreenHeight(this) >= 1920) {
            RelativeLayout.MarginLayoutParams params = (RelativeLayout.MarginLayoutParams) llSelectGrade.getLayoutParams();
            params.topMargin = RxImageTool.dip2px(RxDeviceTool.getScreenHeight(this) / 10);
            llSelectGrade.setLayoutParams(params);
        }

        final List<String> smallGrades = Arrays.asList(getResources().getStringArray(R.array.small_grade));
        smallGradeView.setContents(smallGrades);
        middleGradeView.setContents(Arrays.asList(getResources().getStringArray(R.array.middle_grade)));
        seniorGradeView.setContents(Arrays.asList(getResources().getStringArray(R.array.senior_grade)));

//        smallGradeView.post(new Runnable() {
//            @Override
//            public void run() {
//                smallGradeView.click(0);
//                RxSPTool.putString(SplashActivity.this, SpConstant.SELECT_GRADE, smallGrades.get(0));
//                switchActivity();
//            }
//        });
        setSelectState(smallGradeView);
        setSelectState(middleGradeView);
        setSelectState(seniorGradeView);

    }


    private void setSelectState(final SelectGradeView view) {
        view.setOnSelectGradeListener(new SelectGradeView.OnSelectGradeListener() {
            @Override
            public void onSelect(int position, String data) {
                middleGradeView.clearSelect();
                seniorGradeView.clearSelect();
                smallGradeView.clearSelect();
                view.click(position);
                RxSPTool.putString(SplashActivity.this, SpConstant.SELECT_GRADE, data);
                switchActivity(Time);
            }
        });

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTransparentForWindow(this);
    }


    @Override
    protected void onResume() {
        super.onResume();

        AdvDispatchManager.getManager().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        AdvDispatchManager.getManager().onPause();

    }

    //防止用户返回键退出 APP
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onShow() {
        iv.setVisibility(View.GONE);
    }

    @Override
    public void onDismiss(long delayTime) {
        goActivity(delayTime);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onNativeExpressDismiss(NativeExpressADView view) {

    }

    @Override
    public void onNativeExpressShow(Map<NativeExpressADView, Integer> mDatas) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AdvDispatchManager.getManager().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

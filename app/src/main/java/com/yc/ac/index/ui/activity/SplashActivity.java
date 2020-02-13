package com.yc.ac.index.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.jakewharton.rxbinding.view.RxView;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.vondear.rxtools.RxDeviceTool;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.RxSPTool;
import com.yc.ac.R;
import com.yc.ac.base.Config;
import com.yc.ac.base.MainActivity;
import com.yc.ac.constant.SpConstant;
import com.yc.ac.index.ui.widget.SelectGradeView;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.StatusBarUtil;
import yc.com.tencent_adv.AdvDispatchManager;
import yc.com.tencent_adv.AdvType;
import yc.com.tencent_adv.OnAdvStateListener;
import yc.com.toutiao_adv.TTAdDispatchManager;
import yc.com.toutiao_adv.TTAdType;

/**
 * Created by wanglin  on 2018/3/15 14:11.
 */

public class SplashActivity extends BaseActivity implements OnAdvStateListener, yc.com.toutiao_adv.OnAdvStateListener {

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

    private final int Time = 1000;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void init() {

        rlSelectGrade.setVisibility(View.GONE);
//        iv.setVisibility(View.VISIBLE);

//        AdvDispatchManager.getManager().init(this, AdvType.SPLASH, splashContainer, skipView, Config.tencent_media_id, Config.tencent_splash_id, this);
        TTAdDispatchManager.getManager().init(this, TTAdType.SPLASH, splashContainer, Config.toutiao_splash_id, 0,null, null, 0, null, 0, this);

//        switchActivity();
//        if (RxSPTool.getBoolean(this, SpConstant.IS_FIRST_OPEN)) {
//            rlSelectGrade.setVisibility(View.GONE);
//            iv.setVisibility(View.VISIBLE);
//            switchActivity();
//
//        } else {
//            RxSPTool.putBoolean(this, SpConstant.IS_FIRST_OPEN, true);
//            rlSelectGrade.setVisibility(View.VISIBLE);
//            iv.setVisibility(View.GONE);
//        }
//        initSelectView();
//        initListener();
    }

    private void switchActivity(long delayTime) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, delayTime);
    }

    private void initListener() {
        RxView.clicks(tvSkip).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                switchActivity(Time);
            }
        });
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
    public void onShow() {
        iv.setVisibility(View.GONE);
        skipView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDismiss(long delayTime) {
        switchActivity(delayTime);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        AdvDispatchManager.getManager().onPause();
        TTAdDispatchManager.getManager().onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        AdvDispatchManager.getManager().onResume();
        TTAdDispatchManager.getManager().onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AdvDispatchManager.getManager().onRequestPermissionsResult(requestCode, permissions, grantResults);
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
    public void loadSuccess() {
        switchActivity(0);
    }

    @Override
    public void loadFailed() {
        switchActivity(0);

    }

    @Override
    public void clickAD() {
        switchActivity(0);
    }

    @Override
    public void onTTNativeExpressed(Map<TTNativeExpressAd, Integer> mDatas) {

    }

    @Override
    public void onNativeExpressDismiss(TTNativeExpressAd view) {

    }


}

package com.yc.answer.index.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;
import com.vondear.rxtools.RxDeviceTool;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.RxSPTool;
import com.yc.answer.R;
import com.yc.answer.base.Constant;
import com.yc.answer.base.MainActivity;
import com.yc.answer.constant.SpConstant;
import com.yc.answer.index.ui.widget.SelectGradeView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.StatusBarUtil;

/**
 * Created by wanglin  on 2018/3/15 14:11.
 */

public class SplashActivity extends BaseActivity implements SplashADListener {

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
    private long featchAdTime;
    private static final int Time = 1000;
    private boolean canJump;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void init() {
        if (RxSPTool.getBoolean(this, SpConstant.IS_FIRST_OPEN)) {
            rlSelectGrade.setVisibility(View.GONE);
            iv.setVisibility(View.VISIBLE);
            // 如果targetSDKVersion >= 23，就要申请好权限。如果您的App没有适配到Android6.0（即targetSDKVersion < 23），那么只需要在这里直接调用fetchSplashAD接口。
            if (Build.VERSION.SDK_INT >= 23) {
                checkAndRequestPermission();
            } else {
                // 如果是Android6.0以下的机器，默认在安装时获得了所有权限，可以直接调用SDK
                showSplasAdv();
            }

        } else {
            RxSPTool.putBoolean(this, SpConstant.IS_FIRST_OPEN, true);
            rlSelectGrade.setVisibility(View.VISIBLE);
            iv.setVisibility(View.GONE);
        }
        initSelectView();
        initListener();
    }

    private void switchActivity(long delay) {

        long delayTime = 0;
        if (delay < Time) {
            delayTime = Time - delay;
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);

                startActivity(intent);
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

    private void showSplasAdv() {
        featchAdTime = System.currentTimeMillis();
        SplashAD splashAD = new SplashAD(this, splashContainer, skipView, Constant.TENCENT_ADV_ID, Constant.SPLASH_ADV_ID, this, 0);


    }


    /**
     * ----------非常重要----------
     * <p>
     * Android6.0以上的权限适配简单示例：
     * <p>
     * 如果targetSDKVersion >= 23，那么必须要申请到所需要的权限，再调用广点通SDK，否则广点通SDK不会工作。
     * <p>
     * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
     * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用广点通SDK即可。
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // 权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {
            showSplasAdv();
        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
            showSplasAdv();
        } else {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onADDismissed() {
        if (canJump) {
            switchActivity(Time);
        } else {
            canJump = true;
        }
    }

    @Override
    public void onNoAD(AdError adError) {
        long alreadyDelayMills = System.currentTimeMillis() - featchAdTime;//从拉广告开始到onNoAD已经消耗了多少时间
        switchActivity(alreadyDelayMills);
    }

    @Override
    public void onADPresent() {
        iv.setVisibility(View.GONE);
    }

    @Override
    public void onADClicked() {

    }

    @Override
    public void onADTick(long l) {
        skipView.setText(String.format(getString(R.string.click_to_skip),
                Math.round(l / 1000f)));
    }

    @Override
    public void onADExposure() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canJump) {
            switchActivity(Time);
        }
        canJump = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        canJump = false;

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
}

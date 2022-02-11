package com.yc.ac.setting.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.tencent.bugly.beta.Beta;
import com.vondear.rxtools.RxAppTool;
import com.vondear.rxtools.RxPermissionsTool;
import com.vondear.rxtools.RxPhotoTool;
import com.yc.ac.R;
import com.yc.ac.base.ExitFragment;
import com.yc.ac.constant.BusAction;
import com.yc.ac.setting.contract.SettingContract;
import com.yc.ac.setting.model.bean.UserInfo;
import com.yc.ac.setting.presenter.SettingPresenter;
import com.yc.ac.setting.ui.widget.BaseSettingView;
import com.yc.ac.utils.IvAvatarHelper;
import com.yc.ac.utils.ToastUtils;
import com.yc.ac.utils.UserInfoHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/3/9 14:24.
 */

public class SettingActivity extends BaseActivity<SettingPresenter> implements SettingContract.View {
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.baseSettingView_face)
    BaseSettingView baseSettingViewFace;
    @BindView(R.id.baseSettingView_phone)
    BaseSettingView baseSettingViewPhone;
    @BindView(R.id.baseSettingView_cache)
    BaseSettingView baseSettingViewCache;
    @BindView(R.id.baseSettingView_version)
    BaseSettingView baseSettingViewVersion;
    @BindView(R.id.baseSettingView_exit)
    BaseSettingView baseSettingViewExit;


    private String NOT_BIND = "未绑定";
    private String GO_CHANGE = "去修改";

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void init() {
        commonTvTitle.setText(getString(R.string.setting));
        mPresenter = new SettingPresenter(this, this);

        btnLogout.setVisibility(UserInfoHelper.isLogin() ? View.VISIBLE : View.GONE);

        baseSettingViewVersion.setExtraText(RxAppTool.getAppVersionName(this));

        updateInfo(UserInfoHelper.getUserInfo());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnLogout.setBackgroundResource(R.drawable.btn_ripper_bg);
        }

        initListener();
    }

    private void initListener() {
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        RxView.clicks(btnLogout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mPresenter.logout();
            }
        });

        RxView.clicks(baseSettingViewExit).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //注销账号
                ExitFragment exitFragment = new ExitFragment();
                exitFragment.setTintContent("是否注销账号？\n注销账号后购买的的VIP将无法使用，请谨慎选择！");
                exitFragment.show(getSupportFragmentManager(), "");
                exitFragment.setOnConfirmListener(() -> {
                    mPresenter.userCancellation();
                    exitFragment.dismiss();
                });
            }
        });

        RxView.clicks(baseSettingViewCache).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (mPresenter.clearCache()) {
                    baseSettingViewCache.setExtraText("0KB");
                    ToastUtils.showCenterToast(SettingActivity.this, "缓存清除成功");
                }

            }
        });
        RxView.clicks(baseSettingViewVersion).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Beta.checkUpgrade(true, false);
            }
        });
        RxView.clicks(baseSettingViewFace).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!UserInfoHelper.isGoToLogin(SettingActivity.this)) {

                    applyPermission(() -> RxPhotoTool.openLocalImage(SettingActivity.this));

                }
            }
        });
        RxView.clicks(baseSettingViewPhone).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!UserInfoHelper.isGoToLogin(SettingActivity.this)) {
                    Intent intent = new Intent(SettingActivity.this, BindPhoneActivity.class);
                    if (NOT_BIND.equals(baseSettingViewPhone.getExtraText())) {
                        intent.putExtra("flag", "绑定");

                    } else if (GO_CHANGE.equals(baseSettingViewPhone.getExtraText())) {
                        intent.putExtra("flag", "修改");
                    }
                    startActivity(intent);
                }
            }
        });

    }

    private void applyPermission(Runnable runnable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = RxPermissionsTool.
                    with(this).
                    addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).
                    addPermission(Manifest.permission.READ_EXTERNAL_STORAGE).
                    initPermission();
            if (permissions.size() == 0) {
                runnable.run();
            }
        } else {
            runnable.run();
        }


    }


    @Override
    public void showLogout() {

        btnLogout.setVisibility(View.GONE);
        baseSettingViewFace.clearIcon();
        baseSettingViewPhone.setExtraText("");
    }

    @Override
    public void showCacheSize(String cacheSize) {
        baseSettingViewCache.setExtraText(cacheSize);
    }

    @Override
    public void cancellationSuccess() {
        UserInfoHelper.saveUserInfo(null);
        UserInfoHelper.setToken("");
        RxBus.get().post(BusAction.LOGIN_OUT, "logout");
        finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        IvAvatarHelper.onActivityResult(this, requestCode, resultCode, data);
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.GET_PICTURE)
            }
    )
    public void getPicture(Uri uri) {


        String path = RxPhotoTool.getImageAbsolutePath(this, uri);
        File file = new File(path);
        mPresenter.uploadFile(file, path.substring(path.lastIndexOf("/") + 1));
    }


    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {@Tag(BusAction.LOGIN_SUCCESS)
            }
    )
    public void updateInfo(UserInfo userInfo) {
        if (userInfo != null) {
            baseSettingViewFace.setIvIcon(userInfo.getFace());
            if (TextUtils.isEmpty(userInfo.getMobile())) {
                baseSettingViewPhone.setExtraText(NOT_BIND);
            } else {
                baseSettingViewPhone.setExtraText(GO_CHANGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            List<String> results = new ArrayList<>();
            for (String permission : permissions) {
//                LogUtil.msg("TAG: " + permission);
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    results.add(permission);
                }
            }
            if (results.size() == 0) {
                RxPhotoTool.openLocalImage(SettingActivity.this);
            }

        }
    }



}

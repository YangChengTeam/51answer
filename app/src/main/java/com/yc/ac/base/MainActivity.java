package com.yc.ac.base;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Build;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.umeng.socialize.UMShareAPI;
import com.vondear.rxtools.RxPermissionsTool;
import com.vondear.rxtools.RxSPTool;
import com.yc.ac.R;
import com.yc.ac.collect.ui.fragment.CollectFragment;
import com.yc.ac.constant.SpConstant;
import com.yc.ac.index.ui.activity.ScanTintActivity;
import com.yc.ac.index.ui.fragment.IndexFragment;
import com.yc.ac.index.ui.fragment.PolicyTintFragment;
import com.yc.ac.setting.ui.fragment.MyFragment;
import com.yc.ac.utils.ActivityScanHelper;
import com.yc.ac.utils.IvAvatarHelper;
import com.yc.ac.utils.RxDownloadManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.StatusBarUtil;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, BottomNavigationBar.OnTabSelectedListener, BaseBottomView.onTabSelectedListener {


    @BindView(R.id.main_viewpager)
    ViewPager mMainViewPager;
    @BindView(R.id.main_bottom_navigation_bar)
    BottomNavigationBar mainBottomNavigationBar;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.iv_code_tint)
    ImageView mIvCodeTint;

    @BindView(R.id.container)
    RelativeLayout container;
    private List<Fragment> mList;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        mList = new ArrayList<>();
        mList.add(new IndexFragment());
        mList.add(new CollectFragment());
        mList.add(new MyFragment());



        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager(), mList);


        mMainViewPager.setAdapter(mainAdapter); //视图加载适配器
        mMainViewPager.addOnPageChangeListener(this);
        mMainViewPager.setOffscreenPageLimit(2);
        mMainViewPager.setCurrentItem(0);

        mainBottomNavigationBar.setMode(BottomNavigationBar.MODE_DEFAULT);
        mainBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
//        mainBottomNavigationBar.setBarBackgroundColor(android.R.color.transparent);

        mainBottomNavigationBar.addItem((new BottomNavigationItem(R.mipmap.index_select, getString(R.string.main_index)).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.index_unselect))).setInActiveColorResource(R.color.gray_bfbfbf).setActiveColorResource(R.color.red_f14343))
                .addItem((new BottomNavigationItem(R.mipmap.collect_select, getString(R.string.main_collect)).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.collect_unselect))).setInActiveColorResource(R.color.gray_bfbfbf).setActiveColorResource(R.color.red_f14343))
                .addItem((new BottomNavigationItem(R.mipmap.my_select, getString(R.string.main_my)).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.my_unselect))).setInActiveColorResource(R.color.gray_bfbfbf).setActiveColorResource(R.color.red_f14343))
                .setFirstSelectedPosition(0)
                .initialise();

        mainBottomNavigationBar.setTabSelectedListener(this);

        RxView.clicks(floatingActionButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                if (!RxSPTool.getBoolean(MainActivity.this, SpConstant.IS_OPEN_SCAN)) {
                    startActivity(new Intent(MainActivity.this, ScanTintActivity.class));
                    RxSPTool.putBoolean(MainActivity.this, SpConstant.IS_OPEN_SCAN, true);
                } else {
                    ActivityScanHelper.startScanerCode(MainActivity.this);
                }

            }
        });


    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mainBottomNavigationBar.selectTab(position);
//        if (position == 2) {
//            floatingActionButton.setVisibility(View.GONE);
//        } else {
//            floatingActionButton.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(int position) {
        mMainViewPager.setCurrentItem(position);
//        if (position == 2) {
//            floatingActionButton.setVisibility(View.GONE);
//        } else {
//            floatingActionButton.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void onTabUnselected(int position) {
    }

    @Override
    public void onTabReselected(int position) {

    }

    /**
     * Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,
     * Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,
     * Manifest.permission.SET_DEBUG_APP,
     * Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS
     */
    private void applyPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
            RxPermissionsTool.
                    with(this).
                    addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).
                    addPermission(Manifest.permission.ACCESS_COARSE_LOCATION).
                    addPermission(Manifest.permission.ACCESS_FINE_LOCATION).
                    addPermission(Manifest.permission.READ_EXTERNAL_STORAGE).
                    initPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (String permission : permissions) {
                LogUtil.msg("TAG: " + permission);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        IvAvatarHelper.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {

        final ExitFragment exitFragment = new ExitFragment();
        exitFragment.show(getSupportFragmentManager(), "");
        exitFragment.setOnConfirmListener(new ExitFragment.onConfirmListener() {
            @Override
            public void onConfirm() {
                exitFragment.dismiss();
                RxDownloadManager.getInstance(MainActivity.this).destroy();
                finish();
                System.exit(0);
            }
        });
    }


    // 设置动画
    private void setAnimators() {
        AnimatorSet mOutSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.rotate_out);
        AnimatorSet mInSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.rotate_in);

        mInSet.setTarget(floatingActionButton);

        mOutSet.start();

        mInSet.start();
    }


    private void initBottomView() {
//        mainBaseBottomView.setTabSelectedListener(this);
    }

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, null);
    }
}

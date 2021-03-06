package com.yc.answer.base;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.umeng.socialize.UMShareAPI;
import com.vondear.rxtools.RxPermissionsTool;
import com.vondear.rxtools.RxSPTool;
import com.yc.answer.R;
import com.yc.answer.collect.ui.fragment.CollectFragment;
import com.yc.answer.constant.SpConstant;
import com.yc.answer.index.ui.activity.ScanTintActivity;
import com.yc.answer.index.ui.fragment.IndexFragment;
import com.yc.answer.index.ui.fragment.IndexFragmentNew;
import com.yc.answer.setting.ui.fragment.MyFragment;
import com.yc.answer.utils.ActivityScanHelper;
import com.yc.answer.utils.IvAvatarHelper;
import com.yc.answer.utils.RxDownloadManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import yc.com.base.BaseActivity;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, BottomNavigationBar.OnTabSelectedListener, BaseBottomView.onTabSelectedListener {


    @BindView(R.id.main_viewpager)
    ViewPager mMainViewPager;
    @BindView(R.id.main_bottom_navigation_bar)
    BottomNavigationBar mainBottomNavigationBar;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.iv_code_tint)
    ImageView mIvCodeTint;
    @BindView(R.id.main_base_bottom_view)
    BaseBottomView mainBaseBottomView;
    private List<Fragment> mList;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        mList = new ArrayList<>();
        mList.add(new IndexFragmentNew());
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


        applyPermission();
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

    private void applyPermission() {
        RxPermissionsTool.
                with(this).
                addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.CAMERA).
                addPermission(Manifest.permission.ACCESS_COARSE_LOCATION).
                addPermission(Manifest.permission.ACCESS_FINE_LOCATION).
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


    private void initBottomView(){
        mainBaseBottomView.setTabSelectedListener(this);
    }

    @Override
    public void onTabSelect(int position) {

    }
}

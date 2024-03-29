package com.yc.ac.index.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.google.android.material.tabs.TabLayout;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.umeng.socialize.UMShareAPI;
import com.vondear.rxtools.RxPermissionsTool;
import com.vondear.rxtools.RxSPTool;
import com.yc.ac.R;
import com.yc.ac.base.Config;
import com.yc.ac.base.MyApp;
import com.yc.ac.base.MyRelativeLayout;
import com.yc.ac.base.StateView;
import com.yc.ac.constant.BusAction;
import com.yc.ac.index.contract.AnswerDetailContract;
import com.yc.ac.index.model.bean.BookInfo;
import com.yc.ac.index.presenter.AnswerDetailPresenter;
import com.yc.ac.index.ui.adapter.AnswerDetailAdapter;
import com.yc.ac.index.ui.fragment.AnswerTintFragment;
import com.yc.ac.index.ui.fragment.DeleteTintFragment;
import com.yc.ac.index.ui.fragment.ShareFragment;
import com.yc.ac.pay.PaySuccessInfo;
import com.yc.ac.setting.model.bean.BrowserInfo;
import com.yc.ac.setting.model.bean.UserInfo;
import com.yc.ac.utils.RxDownloadManager;
import com.yc.ac.utils.ToastUtils;
import com.yc.ac.utils.UserInfoHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import yc.com.base.BaseActivity;
import yc.com.base.CacheUtils;
import yc.com.base.FileUtils;
import yc.com.toutiao_adv.OnAdvStateListener;
import yc.com.toutiao_adv.TTAdDispatchManager;
import yc.com.toutiao_adv.TTAdType;

/**
 * Created by wanglin  on 2018/3/12 10:58.
 */

public class AnswerDetailActivity extends BaseActivity<AnswerDetailPresenter> implements AnswerDetailContract.View, OnAdvStateListener {


    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.tv_current_page)
    TextView tvCurrentPage;
    @BindView(R.id.tv_sum_page)
    TextView tvSumPage;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.ll_top_guide)
    LinearLayout llTopGuide;
    @BindView(R.id.tv_collect)
    TextView tvCollect;
    @BindView(R.id.rl_collect)
    RelativeLayout rlCollect;
    @BindView(R.id.tv_downLoad)
    TextView tvDownLoad;
    @BindView(R.id.rl_download)
    RelativeLayout rlDownload;
    @BindView(R.id.rl_share)
    RelativeLayout rlShare;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.m_viewpager)
    ViewPager mViewpager;

    @BindView(R.id.tv_share)
    TextView tvShare;

    @BindView(R.id.ll_container)
    MyRelativeLayout llContainer;

    @BindView(R.id.fl_ad_container)
    FrameLayout flAdContainer;

    @BindView(R.id.iv_scale_icon)
    ImageView ivScaleIcon;
    @BindView(R.id.rootView)
    ConstraintLayout rootView;


    private BookInfo bookInfo;


    private Drawable collectDrawable;
    private Drawable unCollectDrawable;

    private List<String> downLoadUrlList;
    private String bookId;

    private boolean isNeedReadVideo = true;


    @Override
    public int getLayoutId() {
        return R.layout.activity_book_answer_detail;
    }


    public static void startActivity(Context context, String bookName, String bookId) {
        Intent intent = new Intent(context, AnswerDetailActivity.class);
        intent.putExtra("bookName", bookName);
        intent.putExtra("bookId", bookId);
        context.startActivity(intent);
    }


    @Override
    public void init() {
        if (getIntent() != null) {


            String bookName = getIntent().getStringExtra("bookName");
            bookId = getIntent().getStringExtra("bookId");

            commonTvTitle.setText(bookName);

        }

        mPresenter = new AnswerDetailPresenter(this, this);

        collectDrawable = getResources().getDrawable(R.mipmap.collect_select_icon);
        unCollectDrawable = getResources().getDrawable(R.mipmap.collect_unselect_icon);

        getData(false);
        initListener();

        if (MyApp.state == 1) {

            TTAdDispatchManager.getManager().init(this, TTAdType.BANNER, flAdContainer, Config.toutiao_banner1_id, 0, null, null, 0, null, 0, this);
        }
        timerTask = new MyTask();
    }


    private int browserPage = 1;//浏览的页数

    private void setCollectDrawable(boolean isCollect) {

        if (isCollect) {
            collectDrawable.setBounds(0, 0, collectDrawable.getMinimumWidth(), collectDrawable.getMinimumHeight());
            tvCollect.setCompoundDrawables(collectDrawable, null, null, null);
            tvCollect.setTextColor(ContextCompat.getColor(this, R.color.red_f14343));
            tvCollect.setText(getString(R.string.main_collect));
        } else {
            unCollectDrawable.setBounds(0, 0, collectDrawable.getMinimumWidth(), collectDrawable.getMinimumHeight());
            tvCollect.setCompoundDrawables(unCollectDrawable, null, null, null);
            tvCollect.setTextColor(ContextCompat.getColor(this, R.color.black));
            if (isCollectClick)
                tvCollect.setText(getString(R.string.collect_cancel));
        }

    }


    private int oldPos = 0;
    private boolean isCollectClick = false;

    private void initListener() {

        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> finish());


        RxView.clicks(rlCollect).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            isCollectClick = true;
            if (UserInfoHelper.isLogin()) {
                mPresenter.favoriteAnswer(bookInfo);
            } else {
                mPresenter.saveBook(bookInfo);
            }


        });

        //            }
        RxView.clicks(rlDownload).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
//                if (!UserInfoHelper.isGoToLogin(AnswerDetailActivity.this)) {
            TTAdDispatchManager.getManager().init(AnswerDetailActivity.this, TTAdType.REWARD_VIDEO, null, Config.toutiao_reward_id, 0, null, "高清解析", 1, UserInfoHelper.getUId(), TTAdConstant.VERTICAL, AnswerDetailActivity.this);

        });

        //            }
        RxView.clicks(rlShare).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
//            applyPermission(this::showShareDialog);
            showShareDialog();
        });

    }

    private void showShareDialog() {
        ShareFragment shareFragment = new ShareFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bookInfo", bookInfo);

        shareFragment.setArguments(bundle);
        shareFragment.show(getSupportFragmentManager(), null);
    }

    //下载
    private void download() {
        if (judgeIsDownload()) {//已经下载
            DeleteTintFragment deleteTintFragment = new DeleteTintFragment();
            deleteTintFragment.show(getSupportFragmentManager(), "delete");
            deleteTintFragment.setOnConfirmListener(this::deleteBook);
        } else {
            if (bookInfo != null && bookInfo.getAccess() == 0) {
                ToastUtils.showCenterToast(AnswerDetailActivity.this, "分享之后才能下载");
                return;
            }
            if (downLoadUrlList != null && downLoadUrlList.size() > 0) {
                RxDownloadManager.getInstance(AnswerDetailActivity.this).downLoad(downLoadUrlList, bookInfo.getBookId());
            }
        }
    }

    private void getData(boolean isReload) {

        mPresenter.getAnswerDetailInfo(bookId, isReload);
    }

    @Override
    public void showAnswerDetailInfo(BookInfo data, boolean isReload) {
        commonTvTitle.setText(data.getName());
        if (data.getAnswer_list() != null) {
            bookInfo = data;

            initView(data, isReload);
        }
    }

    private void initView(BookInfo data, boolean isReload) {
        if (data != null) {
            setCollectDrawable(data.getFavorite() == 1);
//            downLoadUrlList = addNewData(data.getAnswer_list());
            downLoadUrlList = data.getAnswer_list();
            tvShare.setText(data.getAccess() == 1 ? getString(R.string.shared) : getString(R.string.share));

            if (!isReload) {
                initViewPager(data.getAnswer_list());
//                saveBrowserData(data);
            }
        }
    }

    private List<String> addNewData(List<String> dataList) {
        List<String> newData = new ArrayList<>();
        if (dataList != null && dataList.size() > 0) {
            for (String s : dataList) {
                newData.add(s + "@!ys1200");
            }
        }
        return newData;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViewPager(List<String> answerList) {
//        tvDownLoad.setText(judgeIsDownload() ? "已下载" : "下载");
        tvSumPage.setText(String.valueOf(answerList.size()));
        AnswerDetailAdapter answerDetailAdapter = new AnswerDetailAdapter(this, answerList);
        mViewpager.setAdapter(answerDetailAdapter);
        mViewpager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(mViewpager);

        oldPos = RxSPTool.getInt(this, bookId);
        if (oldPos != -1) {
            if ((bookInfo != null && bookInfo.getAccess() == 1) || oldPos <= 3) {
                mViewpager.setCurrentItem(oldPos);
                tvCurrentPage.setText(String.valueOf(oldPos + 1));
                tabLayout.post(() -> tabLayout.setScrollPosition(oldPos, 0f, true));
            }
        }
        answerDetailAdapter.setOnViewClickListener(new AnswerDetailAdapter.onViewClickListener() {
            @Override
            public void onViewSingleClick(boolean isClick) {

                llTopGuide.setVisibility(isClick ? View.INVISIBLE : View.VISIBLE);
                llBottom.setVisibility(isClick ? View.INVISIBLE : View.VISIBLE);
                llContainer.setBackgroundColor(ContextCompat.getColor(AnswerDetailActivity.this, R.color.white));
            }

            @Override
            public void onViewDoubleClick(boolean isClick) {
                llTopGuide.setVisibility(isClick ? View.GONE : View.VISIBLE);
                llBottom.setVisibility(isClick ? View.GONE : View.VISIBLE);
                rootView.setVisibility(isClick ? View.GONE : View.VISIBLE);
                llContainer.setBackgroundColor(ContextCompat.getColor(AnswerDetailActivity.this, R.color.black));
            }

        });

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

//!isShare(bookInfo) ||
//                bookInfo.setIsVip(1);
                if (position >= 3) {//isNeedReadVideo

                    if (bookInfo != null && (bookInfo.getIsVip() == 1 && !UserInfoHelper.isVip())) {
                        showAnswerDialog(1);
                        return;
                    }
                    if (!UserInfoHelper.isVip()) {
                        if (!isCanNext(bookInfo)) {
                            showAnswerDialog(0);
                            return;
                        }
                    }

//                    ShareInfo shareInfo = new ShareInfo();
//                    shareInfo.setBook_id(bookId);

                }
                browserPage = position + 1;
                oldPos = position;
                tvCurrentPage.setText(String.valueOf(position + 1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void showAnswerDialog(int state) {
        AnswerTintFragment answerTintFragment = new AnswerTintFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bookInfo", bookInfo);
        bundle.putInt("state", state);
        bundle.putString("articleId", bookId);
        answerTintFragment.setArguments(bundle);
        answerTintFragment.show(getSupportFragmentManager(), "");
        answerTintFragment.setConfirmListener(() -> TTAdDispatchManager.getManager().init(AnswerDetailActivity.this, TTAdType.REWARD_VIDEO, null, Config.toutiao_reward_id, 0, null, "解锁看答案", 1, UserInfoHelper.getUId(), TTAdConstant.VERTICAL, AnswerDetailActivity.this));
        mViewpager.setCurrentItem(oldPos);
    }


    //是否可以下一页
    private boolean isCanNext(BookInfo bookInfo) {
        boolean flag = false;
//        if (bookInfo != null && bookInfo.getIsVip() == 1&&!UserInfoHelper.isVip()) {//需要付费
//            return false;
//        }
        if ((bookInfo != null && bookInfo.getAccess() == 1) || judgeIsDownload()) {
            flag = true;
        }
        return flag;

    }

    @Override
    public void showFavoriteResult(String data, boolean isCollect) {
        ToastUtils.showCenterToast(this, (isCollect ? "收藏" : "取消收藏") + "成功");
        setCollectDrawable(isCollect);

    }

    private int count = 0;

    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.DOWNLOAD)
            })

    public void downloadState(String path) {
        count++;
        String tint = count + " / " + downLoadUrlList.size();
        if (count == downLoadUrlList.size()) {
            tint = "已下载";
            ToastUtils.showCenterToast(this, "这本书已经下载完成");
            RxDownloadManager.getInstance(AnswerDetailActivity.this).currentUrlList.clear();
        }
        tvDownLoad.setText(tint);

    }


    private boolean judgeIsDownload() {
        boolean flag = true;

        if (downLoadUrlList != null && downLoadUrlList.size() > 0) {
            for (String url : downLoadUrlList) {
                boolean isExisted = RxDownloadManager.getInstance(this).isFileExisted(bookId, url);
                if (!isExisted) {
                    flag = false;
                    break;
                }
            }
        }

        return flag;

    }

    public void deleteBook() {
        try {
            FileUtils.deleteFileOrDirectory(new File(CacheUtils.makeBaseDir(AnswerDetailActivity.this, bookId)));
            tvDownLoad.setText("下载");
            count = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        if (FileUtils.getFolderFiles(this, bookId) != null) {
//            for (String s : FileUtils.getFolderFiles(this, bookId)) {
//                LogUtil.msg("TAG: " + s);
//            }
            downLoadUrlList = FileUtils.getFolderFiles(this, bookId);

            initViewPager(FileUtils.getFolderFiles(this, bookId));
            stateView.hide();
            return;
        }
        stateView.showNoNet(llContainer, v -> getData(false));
    }

    @Override
    public void showNoData() {
        stateView.showNoData(llContainer);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(llContainer);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxSPTool.putInt(this, bookId, oldPos);
        saveBrowserData(bookInfo);
    }


    private void saveBrowserData(BookInfo bookInfo) {
        BrowserInfo browserInfo = new BrowserInfo();
        if (bookInfo != null) {
            browserInfo.setBookId(bookInfo.getBookId());
            browserInfo.setCover_img(bookInfo.getCover_img());
            browserInfo.setGrade(bookInfo.getGrade());
            browserInfo.setName(bookInfo.getName());
            browserInfo.setPart_type(bookInfo.getPart_type());
            browserInfo.setPeriod(bookInfo.getPeriod());
            browserInfo.setPress(bookInfo.getPress());
            browserInfo.setSubject(bookInfo.getSubject());
            browserInfo.setVersion(bookInfo.getVersion());
            browserInfo.setYear(bookInfo.getYear());
            mPresenter.saveBrowserInfo(browserInfo, browserPage);
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {@Tag(BusAction.SHARE_SUCCESS)})
    public void shareSuccess(String success) {
        bookInfo.setAccess(1);
        tvShare.setText(getString(R.string.shared));

    }

    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {@Tag(BusAction.LOGIN_SUCCESS)})

    public void reloadData(UserInfo userInfo) {
        getData(true);
    }


//    @Subscribe(thread = EventThread.MAIN_THREAD,
//            tags = {@Tag(BusAction.PAY_SUCCESS)})
//    public void paySuccess(PaySuccessInfo paySuccessInfo) {
//        getData(true);
//    }


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            List<String> results = new ArrayList<>();
            for (String permission : permissions) {
                LogUtil.msg("TAG: " + permission);
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    results.add(permission);
                }
            }
            if (results.size() == 0) {
                showShareDialog();
            }

        }
    }

    @Override
    public void loadSuccess() {

    }

    @Override
    public void loadFailed() {

    }

    @Override
    public void clickAD() {
        isNeedReadVideo = false;
    }

    @Override
    public void onTTNativeExpressed(Map<TTNativeExpressAd, Integer> mDatas) {

    }

    @Override
    public void onNativeExpressDismiss(TTNativeExpressAd view) {

    }

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable timerTask;


    private class MyTask implements Runnable {
        @Override
        public void run() {
            showToast();
            mHandler.postDelayed(this, 3000);
        }
    }

    private void showToast() {
        Toast toast = new Toast(this);
        View view = View.inflate(this, R.layout.toast_center_layout, null);
        TextView sMTv_text = view.findViewById(R.id.tv_text);
        sMTv_text.setText("点击下载  解锁会员专属高清答案");
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onRewardVideoComplete() {
        mHandler.removeCallbacks(timerTask);
        ivScaleIcon.setVisibility(View.VISIBLE);
        llContainer.setIsInterceptTouchEvent(ivScaleIcon.getVisibility() == View.VISIBLE);
        llContainer.setListener(() -> {
            ivScaleIcon.setVisibility(View.GONE);
            llContainer.setIsInterceptTouchEvent(false);
        });
    }

    @Override
    public void loadRewardVideoSuccess() {
        mHandler.post(timerTask);
    }


}

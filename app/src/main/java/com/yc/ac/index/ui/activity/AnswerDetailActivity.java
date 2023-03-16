package com.yc.ac.index.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.kk.utils.ToastUtil;
import com.kk.utils.VUiKit;
import com.umeng.socialize.UMShareAPI;
import com.vondear.rxtools.RxPermissionsTool;
import com.vondear.rxtools.RxSPTool;
import com.yc.ac.R;
import com.yc.ac.base.Config;
import com.yc.ac.base.MyApp;
import com.yc.ac.base.MyRelativeLayout;
import com.yc.ac.base.StateView;
import com.yc.ac.constant.BusAction;
import com.yc.ac.dialog.SignDialogThree;
import com.yc.ac.index.contract.AnswerDetailContract;
import com.yc.ac.index.model.bean.BookInfo;
import com.yc.ac.index.presenter.AnswerDetailPresenter;
import com.yc.ac.index.ui.adapter.AnswerDetailAdapter;
import com.yc.ac.index.ui.fragment.AnswerTintFragment;
import com.yc.ac.index.ui.fragment.DeleteTintFragment;
import com.yc.ac.index.ui.fragment.ShareFragment;
import com.yc.ac.setting.model.bean.BrowserInfo;
import com.yc.ac.setting.model.bean.ShareInfo;
import com.yc.ac.setting.model.bean.UserInfo;
import com.yc.ac.utils.CommonUtils;
import com.yc.ac.utils.GlideHelper;
import com.yc.ac.utils.RxDownloadManager;
import com.yc.ac.utils.ToastUtils;
import com.yc.ac.utils.UserInfoHelper;
import com.yc.ac.utils.adgromore.GromoreNewInsetShowTwo;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import yc.com.base.BaseActivity;
import yc.com.base.CacheUtils;
import yc.com.base.FileUtils;

/**
 * Created by wanglin  on 2018/3/12 10:58.
 */

public class AnswerDetailActivity extends BaseActivity<AnswerDetailPresenter> implements AnswerDetailContract.View {


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

        }
        timerTask = new MyTask();
        initJiVideoDialog();
        initShareSureDialog();
        showInset();
    }

    private void showInset() {
        if (MyApp.state==1){
            VUiKit.postDelayed(300, new Runnable() {
                @Override
                public void run() {
                    GromoreNewInsetShowTwo.getInstance().showInset(new GromoreNewInsetShowTwo.OnNewInsetAdShowCaback() {
                        @Override
                        public void onRewardedAdShow() {

                        }

                        @Override
                        public void onError() {

                        }

                        @Override
                        public void onRewardClick() {

                        }

                        @Override
                        public void onRewardedAdClosed(boolean isVideoClick) {

                        }
                    });
                }
            });
        }
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

        });

        //            }
        RxView.clicks(rlShare).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            if (Build.VERSION.SDK_INT<31){
                applyPermission(this::showJiVideoDialog);
            }else {
                showJiVideoDialog();
            }
          //  applyPermission(this::showJiVideoDialog);
         //   showShareDialog();
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
                            showShareSureDialog();
                           // showAnswerDialog(0);
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

    @Override
    public void showSuccess() {
        if (fenxaignDialog!=null){
            if (fenxaignDialog.getIsShow()){
                fenxaignDialog.setDismiss();
            }
        }
        if (bookInfo!=null){
            bookInfo.setAccess(1);
        }
        if (tvShare!=null){
            tvShare.setText(getString(R.string.shared));
        }
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
                showJiVideoDialog();
            }

        }
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


    private ImageView ivBookCover,ivQq,ivWx,ivCode,iv_close;
    private TextView tvAnswerTitle,tvAnswerExtra;
    private LinearLayout llShare,line_share;
    private SignDialogThree fenxaignDialog;

    public void initJiVideoDialog() {//
        fenxaignDialog = new SignDialogThree(this);
        View builder = fenxaignDialog.builder(R.layout.fragment_share_code);
        ivBookCover=builder.findViewById(R.id.iv_book_cover);
        ivQq=builder.findViewById(R.id.iv_qq);
        ivWx=builder.findViewById(R.id.iv_wx);
        ivCode=builder.findViewById(R.id.iv_code);
        tvAnswerTitle=builder.findViewById(R.id.tv_answer_title);
        tvAnswerExtra=builder.findViewById(R.id.tv_answer_extra);
        llShare=builder.findViewById(R.id.ll_share);
        line_share=builder.findViewById(R.id.line_share);
        iv_close=builder.findViewById(R.id.iv_close);
        fenxaignDialog.setOutCancle(true);

    }

    public void showJiVideoDialog() {//
        if (fenxaignDialog!=null&&ivCode!=null){
            tvAnswerTitle.setText(bookInfo.getName());
            tvAnswerExtra.setText(String.format(getString(R.string.share_answer_extra), bookInfo.getSubject(), bookInfo.getGrade(), bookInfo.getPart_type()));
            GlideHelper.loadImage(this, bookInfo.getCover_img(), ivBookCover);
            if (!CommonUtils.isDestory(this)) {
                RxView.clicks(ivWx).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
                    llShare.setVisibility(View.GONE);
                    ivCode.setVisibility(View.VISIBLE);
                    ivCode.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = viewConversionBitmap(line_share);
                            if (bitmap!=null){
                                shareWx(bitmap);
                                mPresenter.share(bookInfo.getBookId());
                                fenxaignDialog.setDismiss();
                            }
                        }
                    }, 500);
                });

                iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fenxaignDialog.setDismiss();
                    }
                });

                RxView.clicks(ivQq).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
                    llShare.setVisibility(View.GONE);
                    ivCode.setVisibility(View.VISIBLE);
                    ivCode.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = viewConversionBitmap(line_share);
                            if (bitmap!=null){
                                shareQQ(bitmap);
                                mPresenter.share(bookInfo.getBookId());
                                fenxaignDialog.setDismiss();
                            }
                        }
                    }, 500);
                });
                if (!CommonUtils.isDestory(this)) {
                    fenxaignDialog.setShow();
                }
            }
        }
    }




    private TextView tvConfirm,tvContent;
    private SignDialogThree sharSureDialog;

    public void initShareSureDialog() {//
        sharSureDialog = new SignDialogThree(this);
        View builder = sharSureDialog.builder(R.layout.dialog_share_code);
        tvConfirm=builder.findViewById(R.id.tv_confirm);
        tvContent=builder.findViewById(R.id.tv_content);
        sharSureDialog.setOutCancle(false);
    }

    public void showShareSureDialog() {//
        if (sharSureDialog!=null){
            if (!CommonUtils.isDestory(this)) {
                tvContent.setText("分享后即可查看完整答案！！");
                tvConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showJiVideoDialog();
                        sharSureDialog.setDismiss();
                    }
                });
                if (!CommonUtils.isDestory(this)) {
                    sharSureDialog.setShow();
                }
            }
        }
    }



    /**
     * 绘制已经测量过的View
     */
    private static Bitmap viewConversionBitmap(View view) {
        Bitmap bitmap=null;
        try {
            int width = view.getWidth();
            int height = view.getHeight();
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
        }catch (Exception e){
            return null;
        }
        return bitmap;
    }




   /* @Override
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
    }*/


    /**
     * 分享图片到微信
     *
     * @param context
     * @param uri
     */
    public void shareToWx(Context context, Uri uri) {
        if (isInstalled(AnswerDetailActivity.this,"com.tencent.mm")) {
            try {
                Intent intent = new Intent();
                ComponentName cop = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
                intent.setComponent(cop);
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(Intent.createChooser(intent, "Share"));
            } catch (Exception e) {
                Toast.makeText(this,"分享失败，请稍后重试",Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this,"请您先安装微信！",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 检测程序是否安装
     *
     * @param packageName
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> installedPackages = manager.getInstalledPackages(0);
        if (installedPackages != null) {
            for (PackageInfo info : installedPackages) {
                if (info.packageName.equals(packageName))
                    return true;
            }
        }
        return false;
    }



    public void shareWx(Bitmap resultPosterBitmap) {
        //Uri tempUri = getImageContentUri(InviteFriendsActivity.this, new File(resultPosterPath));
        Uri tempUri = null;
        try {
            if (Build.VERSION.SDK_INT >= 29) {
                String displayNames="share_img"+System.currentTimeMillis()+"png";
                tempUri = getUri(this, resultPosterBitmap, Bitmap.CompressFormat.PNG, "image/png", displayNames, "caicai");
            } else {
                try {//需要权限
                    tempUri = Uri.parse(MediaStore.Images.Media.insertImage(AnswerDetailActivity.this.getContentResolver(), resultPosterBitmap, null, null));
                }catch (Exception e){

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (tempUri != null) {
            shareToWx(AnswerDetailActivity.this, tempUri);
        } else {
            Toast.makeText(this,"分享失败，请稍后重试",Toast.LENGTH_LONG).show();
        }
    }




    public void shareQQ(Bitmap resultPosterBitmap) {
        //Uri tempUri = getImageContentUri(InviteFriendsActivity.this, new File(resultPosterPath));
        Uri tempUri = null;
        try {
            if (Build.VERSION.SDK_INT >= 29) {
                String displayNames="share_img"+System.currentTimeMillis()+"png";
                tempUri = getUri(this, resultPosterBitmap, Bitmap.CompressFormat.PNG, "image/png", displayNames, null);
            } else {
                try {
                    tempUri = Uri.parse(MediaStore.Images.Media.insertImage(AnswerDetailActivity.this.getContentResolver(), resultPosterBitmap, null, null));
                }catch (Exception e){

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
         if (tempUri != null) {
            shareToQQ(AnswerDetailActivity.this, tempUri);
         } else {
            Toast.makeText(this,"分享失败，请稍后重试1111",Toast.LENGTH_LONG).show();
            return;
        }
    }
    @NonNull
    private Uri getUri(@NonNull final Context context, @NonNull final Bitmap bitmap,
                       @NonNull final Bitmap.CompressFormat format, @NonNull final String mimeType,
                       @NonNull final String displayName, @Nullable final String subFolder) throws IOException {
        String relativeLocation = Environment.DIRECTORY_DCIM;

        if (!TextUtils.isEmpty(subFolder)) {
            relativeLocation += File.separator + subFolder;
        }

        final ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation);

        final ContentResolver resolver = context.getContentResolver();

        OutputStream stream = null;
        Uri uri = null;

        try {
            final Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            uri = resolver.insert(contentUri, contentValues);

            if (uri == null) {
                throw new IOException("Failed to create new MediaStore record.");
            }

            stream = resolver.openOutputStream(uri);

            if (stream == null) {
                throw new IOException("Failed to get output stream.");
            }

            if (bitmap.compress(format, 95, stream) == false) {
                throw new IOException("Failed to save bitmap.");
            }

            return uri;
        } catch (IOException e) {
            if (uri != null) {
                // Don't leave an orphan entry in the MediaStore
                resolver.delete(uri, null, null);
            }

            throw e;
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    /**
     * 分享图片到QQ
     *
     * @param context
     * @param uri
     */
    public void shareToQQ(Context context, Uri uri) {
        if (isInstalled(AnswerDetailActivity.this,"com.tencent.mobileqq")) {
            try {
                Intent intent = new Intent();
                ComponentName cop = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
                intent.setComponent(cop);
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(Intent.createChooser(intent, "Share"));
            } catch (Exception e) {
                Toast.makeText(this,"分享失败，请稍后重试333:"+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this,"请您先安装QQ！",Toast.LENGTH_LONG).show();
        }
    }


}

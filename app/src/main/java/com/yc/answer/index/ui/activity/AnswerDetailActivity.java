package com.yc.answer.index.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.vondear.rxtools.RxSPTool;
import com.yc.answer.R;
import com.yc.answer.base.StateView;
import com.yc.answer.constant.BusAction;
import com.yc.answer.index.contract.AnswerDetailContract;
import com.yc.answer.index.model.bean.BookInfo;
import com.yc.answer.index.presenter.AnswerDetailPresenter;
import com.yc.answer.index.ui.adapter.AnswerDetailAdapter;
import com.yc.answer.index.ui.fragment.AnswerTintFragment;
import com.yc.answer.index.ui.fragment.DeleteTintFragment;
import com.yc.answer.index.ui.fragment.ShareFragment;
import com.yc.answer.setting.model.bean.ShareInfo;
import com.yc.answer.setting.model.bean.UserInfo;

import com.yc.answer.utils.RxDownloadManager;
import com.yc.answer.utils.ToastUtils;
import com.yc.answer.utils.UserInfoHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.CacheUtils;
import yc.com.base.FileUtils;

/**
 * Created by wanglin  on 2018/3/12 10:58.
 */

public class AnswerDetailActivity extends BaseActivity<AnswerDetailPresenter> implements AnswerDetailContract.View {


    @BindView(R.id.iv_back)
    ImageView ivBack;
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
    RelativeLayout llContainer;
    @BindView(R.id.ll_common_container)
    LinearLayout llCommonContainer;
    private BookInfo bookInfo;


    private Drawable collectDrawable;
    private Drawable unCollectDrawable;

    private List<String> downLoadUrlList;
    private String bookId;


    @Override
    public int getLayoutId() {
        return R.layout.activity_book_answer_detail;
    }

    @Override
    public void init() {
        if (getIntent() != null) {

//            bookInfo = getIntent().getParcelableExtra("bookInfo");

            String bookName = getIntent().getStringExtra("bookName");
//                    getIntent().getStringExtra("bookId");
            bookId = getIntent().getStringExtra("bookId");

            commonTvTitle.setText(bookName);

        }

        mPresenter = new AnswerDetailPresenter(this, this);

        collectDrawable = getResources().getDrawable(R.mipmap.collect_select_icon);
        unCollectDrawable = getResources().getDrawable(R.mipmap.collect_unselect_icon);

        getData(false);
        initListener();
    }

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

        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });


        RxView.clicks(rlCollect).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                if (!UserInfoHelper.isGoToLogin(AnswerDetailActivity.this)) {
                    isCollectClick = true;
                    mPresenter.favoriteAnswer(bookInfo);
                }

            }
        });

        RxView.clicks(rlDownload).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!UserInfoHelper.isGoToLogin(AnswerDetailActivity.this)) {
                    if (judgeIsDownload()) {//已经下载
                        DeleteTintFragment deleteTintFragment = new DeleteTintFragment();
                        deleteTintFragment.show(getSupportFragmentManager(), "delete");
                        deleteTintFragment.setOnConfirmListener(new DeleteTintFragment.onConfirmListener() {
                            @Override
                            public void onConfirm() {
                                deleteBook();
                            }
                        });
                    } else {
                        if (bookInfo != null && bookInfo.getAccess() == 0) {
                            ToastUtils.showCenterToast(AnswerDetailActivity.this, "分享之后才能下载");
                            return;
                        }
                        if (downLoadUrlList != null && downLoadUrlList.size() > 0) {
                            RxDownloadManager.getInstance(AnswerDetailActivity.this).downLoad(downLoadUrlList, bookInfo.getId());
                        }
                    }

                }
            }
        });

        RxView.clicks(rlShare).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!UserInfoHelper.isGoToLogin(AnswerDetailActivity.this)) {
                    ShareFragment shareFragment = new ShareFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("bookInfo", bookInfo);

                    shareFragment.setArguments(bundle);
//                    ShareInfo shareInfo = new ShareInfo();
//                    if (bookInfo != null) {
//                        shareInfo.setTitle(bookInfo.getName());
//                        shareInfo.setContent(bookInfo.getShare_content());
//                        shareInfo.setBook_id(bookId);
////                        shareInfo.setUrl(bookInfo.getCover_img());
//                    }
//                    shareFragment.setShareInfo(shareInfo);
                    shareFragment.show(getSupportFragmentManager(), null);
                }
            }
        });

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
            downLoadUrlList = addNewData(data.getAnswer_list());
            tvShare.setText(data.getAccess() == 1 ? getString(R.string.shared) : getString(R.string.share));

            if (!isReload)
                initViewPager(data.getAnswer_list());
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

    private void initViewPager(List<String> answerList) {
        tvDownLoad.setText(judgeIsDownload() ? "已下载" : "下载");
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
                tabLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        tabLayout.setScrollPosition(oldPos, 0f, true);
                    }
                });
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
                llCommonContainer.setVisibility(isClick ? View.GONE : View.VISIBLE);
                llContainer.setBackgroundColor(ContextCompat.getColor(AnswerDetailActivity.this, R.color.black));
            }

        });

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                if (position >= 3 && !isShare(bookInfo)) {

                    AnswerTintFragment answerTintFragment = new AnswerTintFragment();

                    ShareInfo shareInfo = new ShareInfo();
                    shareInfo.setBook_id(bookId);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("share", shareInfo);
                    answerTintFragment.setArguments(bundle);

                    answerTintFragment.show(getSupportFragmentManager(), "");
                    mViewpager.setCurrentItem(oldPos);

                    return;

                }
                oldPos = position;
                tvCurrentPage.setText(String.valueOf(position + 1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private boolean isShare(BookInfo bookInfo) {
        boolean flag = false;
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
        stateView.showNoNet(llContainer, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(false);
            }
        });
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


}

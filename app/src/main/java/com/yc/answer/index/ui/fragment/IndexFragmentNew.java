package com.yc.answer.index.ui.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vondear.rxtools.RxImageTool;
import com.yc.answer.R;
import com.yc.answer.base.BaseBookView;
import com.yc.answer.base.WebActivity;
import com.yc.answer.index.contract.IndexContract;
import com.yc.answer.index.model.bean.BookInfo;
import com.yc.answer.index.model.bean.SlideInfo;
import com.yc.answer.index.model.bean.VersionDetailInfo;
import com.yc.answer.index.presenter.IndexPresenter;
import com.yc.answer.index.ui.activity.AnswerDetailActivity;
import com.yc.answer.index.ui.activity.SearchActivityNew;
import com.yc.answer.index.ui.activity.UploadBookListActivity;
import com.yc.answer.index.ui.adapter.BannerImageLoader;
import com.yc.answer.index.ui.adapter.IndexBookAdapter;
import com.yc.answer.index.ui.widget.BaseSearchView;
import com.yc.answer.index.ui.widget.FilterPopwindow;
import com.yc.answer.index.ui.widget.MyDecoration;
import com.yc.answer.utils.UserInfoHelper;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseFragment;

/**
 * Created by wanglin  on 2018/2/27 14:43.
 */

public class IndexFragmentNew extends BaseFragment<IndexPresenter> implements IndexContract.View {


    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.baseSearchView)
    BaseSearchView baseSearchView;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.baseBookView_first)
    BaseBookView baseBookViewFirst;
    @BindView(R.id.baseBookView_second)
    BaseBookView baseBookViewSecond;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_filter)
    LinearLayout llFilter;
    @BindView(R.id.rl_tab)
    RelativeLayout rlTab;
    @BindView(R.id.rl_plug_filter)
    RelativeLayout rlPlugFilter;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.ll_upload)
    LinearLayout llUpload;


    private IndexBookAdapter hotItemAdapter;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_index_new;
    }

    @Override
    public void init() {
        mPresenter = new IndexPresenter(getActivity(), this);


        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        hotItemAdapter = new IndexBookAdapter(null);
        recyclerView.setAdapter(hotItemAdapter);

        recyclerView.addItemDecoration(new MyDecoration(10, 10));

        banner.setFocusable(false);
        initRefresh();
        initListener();
    }

    private void initListener() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (-verticalOffset >= appBarLayout.getHeight() - RxImageTool.dp2px(130)) {
                    baseSearchView.setSearchStyle(true);
                } else {
                    baseSearchView.setSearchStyle(false);
                }
            }
        });


        RxView.clicks(baseSearchView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), SearchActivityNew.class));
            }
        });
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                SlideInfo slideInfo = mPresenter.getSlideInfo(position);
                if (slideInfo != null && !TextUtils.isEmpty(slideInfo.getLink().trim())) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("url", slideInfo.getLink());
                    startActivity(intent);
                }
            }
        });

        RxView.clicks(llFilter).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
//                FilterPopwindow filterPopwindow = new FilterPopwindow(getActivity());
//                filterPopwindow.showAsDropDown(rlTab);
                Intent intent = new Intent(getActivity(), SearchActivityNew.class);
                startActivity(intent);
            }
        });

//        RxView.clicks(baseBookViewFirst).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                Intent intent = new Intent(getActivity(), AnswerDetailActivity.class);
//                if (firstBookInfo != null) {
//                    intent.putExtra("bookId", firstBookInfo.getId());
//                    intent.putExtra("bookName", firstBookInfo.getName());
//
////                    intent.putExtra("bookInfo",firstBookInfo);
//                }
//                startActivity(intent);
//            }
//        });
//
//        RxView.clicks(baseBookViewSecond).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                Intent intent = new Intent(getActivity(), AnswerDetailActivity.class);
//                if (secondBookInfo != null) {
//                    intent.putExtra("bookId", secondBookInfo.getId());
//                    intent.putExtra("bookName", secondBookInfo.getName());
////                    intent.putExtra("bookInfo",secondBookInfo);
//                }
//                startActivity(intent);
//            }
//        });
        hotItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                String tag = (String) adapter.getItem(position);
//                Intent intent = new Intent(getActivity(), SearchActivity.class);
//                intent.putExtra("text", tag);
//                SearchHistoryHelper.saveHistoryList(tag);
//                intent.putExtra("page", 1);
//                startActivity(intent);
                BookInfo bookInfo = (BookInfo) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), AnswerDetailActivity.class);

                intent.putExtra("bookName", bookInfo.getName());
                intent.putExtra("bookId", bookInfo.getId());
                startActivity(intent);

            }
        });

        RxView.clicks(rlPlugFilter).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                FilterPopwindow filterPopwindow = new FilterPopwindow(getActivity());
                filterPopwindow.showAsDropDown(rlTab);
            }
        });
        RxView.clicks(llUpload).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!UserInfoHelper.isGoToLogin(getActivity())) {
                    Intent intent = new Intent(getActivity(), UploadBookListActivity.class);
                    startActivity(intent);
                }
            }
        });

    }


    @Override
    public void hide() {

    }

    @Override
    public void showNoNet() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void showLoading() {

    }


    @Override
    public void showVersionList(List<VersionDetailInfo> data) {

    }

    @Override
    public void showImgList(List<String> imgList) {
        banner.isAutoPlay(true)
                .setDelayTime(3000)
                .setImageLoader(new BannerImageLoader())
                .setImages(imgList)
                .start();
    }


    @Override
    public void showHotBooks(List<BookInfo> lists) {
        if (lists != null) {
            hotItemAdapter.setNewData(lists);
        }
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh();
        }

    }

    @Override
    public void showConditionList(List<String> data) {


    }

    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    private void initRefresh() {
        //设置 Header 为 BezierRadar 样式
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.autoRefresh();
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getData();
//                mPresenter.loadData(true);

            }
        });
    }

    private void getData() {
        mPresenter.getHotBooks();
        mPresenter.getSlideInfo("home");
        mPresenter.getVersionList();
        mPresenter.getConditionList();

    }


}

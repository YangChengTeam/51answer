package com.yc.answer.index.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.androidkun.xtablayout.XTabLayout;
import com.jakewharton.rxbinding.view.RxView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vondear.rxtools.RxImageTool;
import com.yc.answer.R;
import com.yc.answer.base.StateView;
import com.yc.answer.base.WebActivity;
import com.yc.answer.index.contract.IndexContract;
import com.yc.answer.index.model.bean.BookInfo;
import com.yc.answer.index.model.bean.SlideInfo;
import com.yc.answer.index.model.bean.VersionDetailInfo;
import com.yc.answer.index.presenter.IndexPresenter;
import com.yc.answer.index.ui.activity.SearchActivity;
import com.yc.answer.index.ui.adapter.BannerImageLoader;
import com.yc.answer.index.ui.adapter.IndexPagerAdapter;
import com.yc.answer.index.ui.widget.BaseSearchView;
import com.yc.answer.index.ui.widget.FilterPopwindow;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.functions.Action1;
import yc.com.base.BaseFragment;

/**
 * Created by wanglin  on 2018/2/27 14:43.
 */

public class IndexFragment extends BaseFragment<IndexPresenter> implements IndexContract.View {
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tabLayout)
    XTabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.baseSearchView)
    BaseSearchView baseSearchView;
    @BindView(R.id.ll_filter)
    LinearLayout llFilter;
    @BindView(R.id.ll_tablayout)
    LinearLayout llTablayout;


    private List<Fragment> list;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    public void init() {
        mPresenter = new IndexPresenter(getActivity(), this);
        list = new ArrayList<>();
        banner.setFocusable(false);

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

        RxView.clicks(llFilter).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                FilterPopwindow filterPopwindow = new FilterPopwindow(getActivity());
                filterPopwindow.showAsDropDown(llTablayout);
            }
        });

        RxView.clicks(baseSearchView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                SlideInfo slideInfo = mPresenter.getSlideInfo(position);
                if (slideInfo != null) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("url", slideInfo.getLink());
                    startActivity(intent);
                }
            }
        });

    }


    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(llContainer, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loadData(true);
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
    public void showVersionList(List<VersionDetailInfo> data) {

        for (int i = 0; i < data.size(); i++) {

            IndexBookFragment bookFragment = new IndexBookFragment();
            bookFragment.setSujectId(data.get(i).getId());
            list.add(bookFragment);
        }
        IndexPagerAdapter indexPagerAdapter = new IndexPagerAdapter(getChildFragmentManager(), list, data);
        viewpager.setAdapter(indexPagerAdapter);
        viewpager.setOffscreenPageLimit(4);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewpager);


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

}

package com.yc.ac.index.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vondear.rxtools.RxImageTool;
import com.yc.ac.R;
import com.yc.ac.base.WebActivity;
import com.yc.ac.index.contract.IndexContract;
import com.yc.ac.index.model.bean.BookInfo;
import com.yc.ac.index.model.bean.SlideInfo;
import com.yc.ac.index.model.bean.TagInfo;
import com.yc.ac.index.model.bean.VersionDetailInfo;
import com.yc.ac.index.presenter.IndexPresenter;
import com.yc.ac.index.ui.activity.SearchActivityNew;
import com.yc.ac.index.ui.activity.UploadBookListActivity;
import com.yc.ac.index.ui.adapter.BannerImageLoader;
import com.yc.ac.index.ui.adapter.IndexTagAdapter;
import com.yc.ac.index.ui.adapter.IndexZtAdapter;
import com.yc.ac.index.ui.widget.BaseSearchView;
import com.yc.ac.index.ui.widget.MyDecoration;
import com.yc.ac.setting.ui.activity.SettingActivity;
import com.yc.ac.setting.ui.fragment.ShareFragment;
import com.yc.ac.utils.ShareInfoHelper;
import com.yc.ac.utils.UserInfoHelper;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.functions.Action1;
import yc.com.base.BaseFragment;
import yc.com.base.StatusBarUtil;

/**
 * Created by wanglin  on 2018/2/27 14:43.
 */

public class IndexFragmentNew extends BaseFragment<IndexPresenter> implements IndexContract.View {


    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.baseSearchView)
    BaseSearchView baseSearchView;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.hot_recyclerView)
    RecyclerView hotRecyclerView;
    @BindView(R.id.ll_filter)
    LinearLayout llFilter;
    @BindView(R.id.rl_tab)
    RelativeLayout rlTab;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_upload)
    LinearLayout llUpload;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    @BindView(R.id.tv_hot_tag)
    TextView tvHotTag;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;
    @BindView(R.id.index_iv_logo)
    ImageView indexIvLogo;
    @BindView(R.id.iv_index_share)
    ImageView ivIndexShare;
    @BindView(R.id.iv_index_search)
    ImageView ivIndexSearch;


    private IndexTagAdapter tagAdapter;
    private IndexZtAdapter indexZtAdapter;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_index_new;
    }

    @Override
    public void init() {
//        setStatusBar();
//        StatusBarUtil.setStatusTextColor1(true, getActivity());
        mPresenter = new IndexPresenter(getActivity(), this);


        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//        hotItemAdapter = new IndexBookAdapter(null);
        indexZtAdapter = new IndexZtAdapter(null);
        recyclerView.setAdapter(indexZtAdapter);

        recyclerView.addItemDecoration(new MyDecoration(10, 15));

        hotRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        tagAdapter = new IndexTagAdapter(null);
        hotRecyclerView.setAdapter(tagAdapter);

        hotRecyclerView.addItemDecoration(new MyDecoration(10, 15));
        banner.setFocusable(false);
        initRefresh();
        initListener();

    }

    private void setStatusBar() {
        CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height = StatusBarUtil.getStatusBarHeight(getActivity());
        view.setLayoutParams(layoutParams);
        view.setBackgroundColor(Color.argb(30, 0, 0, 0));
    }


    private void initListener() {

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

//                int appBarHeight = appBarLayout.getHeight() - RxImageTool.dp2px(130);
//                LogUtil.msg("verticalOffset:  " + verticalOffset + "  appBarHeight: " + appBarHeight);

                //verticalOffset  当前偏移量 appBarLayout.getTotalScrollRange() 最大高度 便宜值
                int offset = Math.abs(verticalOffset); //目的是将负数转换为绝对正数；
                if (offset > 0) {
                    //标题栏的渐变
                    toolbar.setVisibility(View.VISIBLE);
                    toolbar.setBackgroundColor(changeAlpha(getResources().getColor(R.color.gray_qian)
                            , Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange()));
                } else {
                    toolbar.setVisibility(View.GONE);
                }

                /**
                 * 当前最大高度偏移值除以2 在减去已偏移值 获取浮动 先显示在隐藏
                 */
                if (offset < appBarLayout.getTotalScrollRange() / 2) {
                    toolbar.setTitle("");
                    float alpha = (appBarLayout.getTotalScrollRange() / 2 - offset * 1.0f) / (appBarLayout.getTotalScrollRange() / 2);
                    toolbar.setAlpha(alpha);
                    rlContainer.setAlpha(alpha);
                    indexIvLogo.setAlpha(alpha);
                    /**
                     * 从最低浮动开始渐显 当前 offset就是  appBarLayout.getTotalScrollRange() / 2
                     * 所以 offset - appBarLayout.getTotalScrollRange() / 2
                     */
                } else if (offset > appBarLayout.getTotalScrollRange() / 2) {
                    float floate = (offset - appBarLayout.getTotalScrollRange() / 2) * 1.0f / (appBarLayout.getTotalScrollRange() / 2);
                    toolbar.setAlpha(floate);
                    rlContainer.setAlpha(floate);
                    indexIvLogo.setAlpha(floate);
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

                Intent intent = new Intent(getActivity(), SearchActivityNew.class);
                startActivity(intent);
            }
        });


        indexZtAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                TagInfo tagInfo = (TagInfo) adapter.getItem(position);
                if (tagInfo != null) {
                    WebActivity.startActivity(getActivity(), tagInfo.getZtpath(), tagInfo.getZtname());

//                    AnswerDetailActivity.startActivity(getActivity(), bookInfo.getName(), bookInfo.getBookId());
                }

            }
        });

        tagAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TagInfo tagInfo = (TagInfo) adapter.getItem(position);
                if (tagInfo != null) {
                    Intent intent = new Intent(getActivity(), SearchActivityNew.class);
                    intent.putExtra("name", tagInfo.getTitle());
                    startActivity(intent);
                }
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
        RxView.clicks(indexIvLogo).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!UserInfoHelper.isGoToLogin(getActivity())) {
                    Intent intent = new Intent(getActivity(), SettingActivity.class);
                    startActivity(intent);
                }
            }
        });
        RxView.clicks(ivIndexSearch).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), SearchActivityNew.class));
            }
        });
        RxView.clicks(ivIndexShare).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ShareFragment shareFragment = new ShareFragment();
                shareFragment.setIsShareMoney(true);
                shareFragment.setShareInfo(ShareInfoHelper.getShareInfo());
                shareFragment.show(getActivity().getSupportFragmentManager(), "");
            }
        });

    }

    /**
     * 根据百分比改变颜色透明度
     */
    public int changeAlpha(int color, float fraction) {
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, 241, 67, 67);
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

        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh();
        }

    }

    @Override
    public void showConditionList(List<String> data) {


    }

    @Override
    public void showTagInfos(List<TagInfo> data) {
        tagAdapter.setNewData(data);
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh();
        }
    }

    @Override
    public void showZtInfos(List<TagInfo> list) {
        indexZtAdapter.setNewData(list);
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh();
        }
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

            }
        });
    }

    private void getData() {
//        mPresenter.getHotBooks();
        mPresenter.getSlideInfo("home");
//        mPresenter.getVersionList();
//        mPresenter.getConditionList();
        mPresenter.getTagInfos();
        mPresenter.getZtInfos();
    }


}
package com.yc.ac.index.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jakewharton.rxbinding.view.RxView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vondear.rxtools.RxSPTool;
import com.yc.ac.R;
import com.yc.ac.base.Config;
import com.yc.ac.base.MyApp;
import com.yc.ac.base.WebActivity;
import com.yc.ac.constant.SpConstant;
import com.yc.ac.index.contract.IndexContract;
import com.yc.ac.index.model.bean.BookInfo;
import com.yc.ac.index.model.bean.SlideInfo;
import com.yc.ac.index.model.bean.TagInfo;
import com.yc.ac.index.model.bean.VersionDetailInfo;
import com.yc.ac.index.presenter.IndexPresenter;
import com.yc.ac.index.ui.activity.SearchActivity;
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

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseFragment;
import yc.com.base.StatusBarUtil;
import yc.com.toutiao_adv.OnAdvStateListener;
import yc.com.toutiao_adv.TTAdDispatchManager;
import yc.com.toutiao_adv.TTAdType;

/**
 * Created by wanglin  on 2018/2/27 14:43.
 */

public class IndexFragment extends BaseFragment<IndexPresenter> implements IndexContract.View, OnAdvStateListener {


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

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_upload)
    LinearLayout llUpload;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;


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
    @BindView(R.id.bottomContainer)
    FrameLayout bottomContainer;


    private IndexTagAdapter tagAdapter;
    private IndexZtAdapter indexZtAdapter;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    public void init() {
//        setStatusBar();
//        StatusBarUtil.setStatusTextColor1(true, getActivity());
        mPresenter = new IndexPresenter(getActivity(), this);

        if (!RxSPTool.getBoolean(getActivity(), SpConstant.INDEX_DIALOG)) {
            IndexDialogFragment indexDialogFragment = new IndexDialogFragment();
            indexDialogFragment.show(getChildFragmentManager(), "");
        }
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
//        AdvDispatchManager.getManager().init(getActivity(), AdvType.BANNER, bottomContainer, null, Config.tencent_media_id, Config.tencent_bottom_banner_id, null);
        if (MyApp.state == 1) {
            TTAdDispatchManager.getManager().init(getActivity(), TTAdType.BANNER, bottomContainer, Config.toutiao_banner1_id, 0, null, null, 0, null, 0, this);
        }
    }


    //是否是指定机型
    private boolean isAssignPhone() {
        return TextUtils.equals("huawei", Build.BRAND.toLowerCase()) || TextUtils.equals("honor", Build.BRAND.toLowerCase());
    }

    private void setStatusBar() {
        CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height = StatusBarUtil.getStatusBarHeight(getActivity());
        view.setLayoutParams(layoutParams);
        view.setBackgroundColor(Color.argb(30, 0, 0, 0));
    }


    private void initListener() {

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {

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
            int scrollRange = appBarLayout.getTotalScrollRange() / 2;
            if (offset < appBarLayout.getTotalScrollRange() / 2) {
                toolbar.setTitle("");
                float alpha = (scrollRange - offset * 1.0f) / (scrollRange);
                toolbar.setAlpha(alpha);
                rlContainer.setAlpha(alpha);
                indexIvLogo.setAlpha(alpha);
                /**
                 * 从最低浮动开始渐显 当前 offset就是  appBarLayout.getTotalScrollRange() / 2
                 * 所以 offset - appBarLayout.getTotalScrollRange() / 2
                 */
            } else if (offset > appBarLayout.getTotalScrollRange() / 2) {
                float floate = (offset - scrollRange) * 1.0f / (scrollRange);
                toolbar.setAlpha(floate);
                rlContainer.setAlpha(floate);
                indexIvLogo.setAlpha(floate);
            }
        });


        RxView.clicks(baseSearchView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> startActivity(new Intent(getActivity(), SearchActivity.class)));
        banner.setOnBannerListener(position -> {
            SlideInfo slideInfo = mPresenter.getSlideInfo(position);
            if (slideInfo != null && !TextUtils.isEmpty(slideInfo.getLink().trim())) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", slideInfo.getLink());
                startActivity(intent);
            }
        });

        RxView.clicks(llFilter).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {

            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });


        indexZtAdapter.setOnItemClickListener((adapter, view, position) -> {

            TagInfo tagInfo = (TagInfo) adapter.getItem(position);
            if (tagInfo != null) {
                WebActivity.startActivity(getActivity(), tagInfo.getZtpath(), tagInfo.getZtname());

//                    AnswerDetailActivity.startActivity(getActivity(), bookInfo.getName(), bookInfo.getBookId());
            }

        });

        tagAdapter.setOnItemClickListener((adapter, view, position) -> {
            TagInfo tagInfo = (TagInfo) adapter.getItem(position);
            if (tagInfo != null) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("name", tagInfo.getTitle());
                startActivity(intent);
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
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
        RxView.clicks(ivIndexShare).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                com.yc.ac.setting.ui.fragment.ShareFragment shareFragment = new ShareFragment();
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
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh();
        }
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
        if (list != null && list.size() > 4) {
            list = list.subList(0, 4);
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        TTAdDispatchManager.getManager().onDestroy();
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

    @Override
    public void loadSuccess() {

    }

    @Override
    public void loadFailed() {

    }

    @Override
    public void clickAD() {

    }

    @Override
    public void onTTNativeExpressed(Map<TTNativeExpressAd, Integer> mDatas) {

    }

    @Override
    public void onNativeExpressDismiss(TTNativeExpressAd view) {

    }

    @Override
    public void onRewardVideoComplete() {

    }

    @Override
    public void loadRewardVideoSuccess() {

    }


}

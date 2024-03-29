package com.yc.ac.collect.ui.fragment;


import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.vondear.rxtools.RxSPTool;
import com.yc.ac.R;
import com.yc.ac.base.Config;
import com.yc.ac.base.MyApp;
import com.yc.ac.base.StateView;
import com.yc.ac.collect.contract.CollectContract;
import com.yc.ac.collect.presenter.CollectPresenter;
import com.yc.ac.constant.BusAction;
import com.yc.ac.constant.SpConstant;
import com.yc.ac.index.model.bean.BookInfo;
import com.yc.ac.index.model.bean.BookInfoWrapper;
import com.yc.ac.index.ui.activity.AnswerDetailActivity;
import com.yc.ac.index.ui.adapter.IndexBookAdapter;
import com.yc.ac.index.ui.widget.MyDecoration;
import com.yc.ac.setting.model.bean.UserInfo;
import com.yc.ac.utils.SpanUtils;
import com.yc.ac.utils.UserInfoHelper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseFragment;
import yc.com.base.UIUtils;
import yc.com.tencent_adv.AdvDispatchManager;
import yc.com.tencent_adv.AdvType;
import yc.com.toutiao_adv.OnAdvStateListener;
import yc.com.toutiao_adv.TTAdDispatchManager;
import yc.com.toutiao_adv.TTAdType;

/**
 * Created by wanglin  on 2018/3/7 13:53.
 */

public class CollectFragment extends BaseFragment<CollectPresenter> implements CollectContract.View, OnAdvStateListener {


    @BindView(R.id.collect_recyclerView)
    RecyclerView collectRecyclerView;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.iv_photograph)
    ImageView ivPhotograph;
    @BindView(R.id.iv_word)
    ImageView ivWord;
    @BindView(R.id.topContainer)
    FrameLayout topContainer;


    private IndexBookAdapter indexBookAdapter;

    private int page = 1;
    private int limit = 15;
    private TextView tvCollectCount;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_collect;
    }

    @Override
    public void init() {

        mPresenter = new CollectPresenter(getActivity(), this);
        ivBack.setVisibility(View.GONE);
        commonTvTitle.setText("我的收藏");

        collectRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        indexBookAdapter = new IndexBookAdapter(null);
        collectRecyclerView.setAdapter(indexBookAdapter);
        collectRecyclerView.addItemDecoration(new MyDecoration(10, 10));

        addHeaderView();

        initListener();
        initRefresh();
        getData(false);
//        AdvDispatchManager.getManager().init(getActivity(), AdvType.BANNER, topContainer, null, Config.tencent_media_id, Config.tencent_top_banner_id, null);
        if (MyApp.state == 1 && RxSPTool.getBoolean(requireActivity(), SpConstant.INDEX_DIALOG))
            TTAdDispatchManager.getManager().init(getActivity(), TTAdType.BANNER, topContainer, Config.toutiao_banner2_id, 0, null, null, 0, null, 0, this);

    }

    private void addHeaderView() {
        View headerView = View.inflate(getActivity(), R.layout.collect_header_view, null);
        tvCollectCount = headerView.findViewById(R.id.tv_header_view);
        indexBookAdapter.addHeaderView(headerView);
    }

    private void initListener() {
        indexBookAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(false);

            }
        }, collectRecyclerView);

        indexBookAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BookInfo bookInfo = (BookInfo) adapter.getItem(position);
                if (bookInfo != null) {
                    AnswerDetailActivity.startActivity(getActivity(), bookInfo.getName(), bookInfo.getBookId());
                }
            }
        });
        RxView.clicks(ivPhotograph).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                switchSmallProcedure("wx622cbf19fcb00f29", "gh_ae8f66b61fcb");
            }
        });

        RxView.clicks(ivWord).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                switchSmallProcedure("wx622cbf19fcb00f29", "gh_85f8523be35e");
            }
        });

    }


    private void switchSmallProcedure(String appId, String originId) {
        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId);

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = originId; // 填小程序原始id
//                    req.path = path;                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
    }

    @Override
    public void showCollectList(BookInfoWrapper data) {

        if (data != null && data.getLists() != null) {
            int count = data.getCount();

            String str = "共收藏 <font color=\"#FF0000\"> " + count + "</font> 个";

            tvCollectCount.setText(Html.fromHtml(str));

            if (page == 1) {
                indexBookAdapter.setNewData(data.getLists());
            } else {
                indexBookAdapter.addData(data.getLists());
            }
            if (data.getLists().size() == limit) {
                page++;
                indexBookAdapter.loadMoreComplete();
            } else {
                indexBookAdapter.loadMoreEnd();
            }

        } else {
            indexBookAdapter.loadMoreEnd();
        }

        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh();
        }


    }

    @Override
    public void showEnd() {
        indexBookAdapter.loadMoreEnd();
    }

    @Override
    public void showTintInfo(CharSequence tint) {

        stateView.showNoData(smartRefreshLayout, tint, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoHelper.isGoToLogin(getActivity());
            }
        });
    }


    public void getData(boolean isRefresh) {

        mPresenter.getCollectList(page, limit, isRefresh);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.COLLECT)
            })
    public void reloadData(String flag) {
        page = 1;
        getData(true);

    }


    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(smartRefreshLayout, "在首页搜索需要的书籍\n点击进入收藏即可哦", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(false);
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(smartRefreshLayout);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(smartRefreshLayout);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.LOGIN_SUCCESS)
            })
    public void reloadData(UserInfo userInfo) {
        getData(false);
    }


    private void initRefresh() {
        //设置 Header 为 BezierRadar 样式
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.autoRefresh();
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getData(true);

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TTAdDispatchManager.getManager().onDestroy();
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

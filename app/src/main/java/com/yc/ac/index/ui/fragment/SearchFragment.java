package com.yc.ac.index.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bytedance.msdk.api.AdError;
import com.bytedance.msdk.api.AdSlot;
import com.bytedance.msdk.api.UIUtils;
import com.bytedance.msdk.api.v2.GMMediationAdSdk;
import com.bytedance.msdk.api.v2.GMSettingConfigCallback;
import com.bytedance.msdk.api.v2.ad.nativeAd.GMNativeAd;
import com.bytedance.msdk.api.v2.ad.nativeAd.GMNativeAdLoadCallback;
import com.bytedance.msdk.api.v2.ad.nativeAd.GMNativeExpressAdListener;
import com.bytedance.msdk.api.v2.ad.nativeAd.GMUnifiedNativeAd;
import com.bytedance.msdk.api.v2.slot.GMAdOptionUtil;
import com.bytedance.msdk.api.v2.slot.GMAdSlotNative;
import com.bytedance.msdk.api.v2.slot.paltform.GMAdSlotGDTOption;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.vondear.rxtools.RxSPTool;
import com.yc.ac.R;
import com.yc.ac.base.Config;
import com.yc.ac.base.MyApp;
import com.yc.ac.base.StateView;
import com.yc.ac.constant.BusAction;
import com.yc.ac.constant.SpConstant;
import com.yc.ac.index.contract.BookConditionContract;
import com.yc.ac.index.model.bean.BookInfo;
import com.yc.ac.index.presenter.BookConditionPresenter;
import com.yc.ac.index.ui.activity.AnswerDetailActivity;
import com.yc.ac.index.ui.activity.UploadBookActivity;
import com.yc.ac.index.ui.adapter.SearchResultItemAdapter;
import com.yc.ac.index.ui.widget.FilterPopWindow;
import com.yc.ac.utils.UserInfoHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseFragment;

/**
 * Created by wanglin  on 2018/3/10 10:09.
 */

public class SearchFragment extends BaseFragment<BookConditionPresenter> implements BookConditionContract.View{


    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.iv_grade)
    ImageView ivGrade;
    @BindView(R.id.ll_grade)
    LinearLayout llGrade;
    @BindView(R.id.tv_subject)
    TextView tvSubject;
    @BindView(R.id.iv_subject)
    ImageView ivSubject;
    @BindView(R.id.ll_subject)
    LinearLayout llSubject;
    @BindView(R.id.tv_part)
    TextView tvPart;
    @BindView(R.id.iv_part)
    ImageView ivPart;
    @BindView(R.id.ll_part)
    LinearLayout llPart;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.iv_version)
    ImageView ivVersion;
    @BindView(R.id.ll_version)
    LinearLayout llVersion;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.search_recyclerView)
    RecyclerView searchRecyclerView;
    @BindView(R.id.tv_feedback)
    TextView tvFeedback;
    @BindView(R.id.ll_top_guide)
    LinearLayout llTopGuide;
    @BindView(R.id.rl_feedback)
    RelativeLayout rlFeedback;


    private String grade;//年级
    private String subject;//学科
    private String part;//上下册
    private String version;//版本

    private String mName;//书名
    private String code;//条形码

    private int page = 1;
    private final int LIMIT = 20;
    private SearchResultItemAdapter itemAdapter;
    private TextView textView;


    public static final int AD_COUNT = 2;// 加载广告的条数，取值范围为[1, 10]


    public static int FIRST_AD_POSITION = 3; // 第一条广告的位置
    public static int SECOND_AD_POSITION = 10; // 第一条广告的位置
    private HashMap<TTNativeExpressAd, Integer> mAdViewPositionMap = new HashMap<>();

    private boolean showBottom = true;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search;
    }

    public String getName() {

        return mName;
    }

    public void setName(String name) {
        this.mName = name;
        code = "";
        page = 1;
        getData();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public void init() {

        mPresenter = new BookConditionPresenter(getActivity(), this);
        if (getArguments() != null) {
            code = getArguments().getString("code");
            mName = getArguments().getString("name");
            showBottom = getArguments().getBoolean("showBottom", true);
        }
        initView();
        initAdapter();
        initListener();


        rlFeedback.setVisibility(showBottom ? View.VISIBLE : View.GONE);
        loadAd();
        loadExpressAd();
    }



    private void loadAd() {
        List<Integer> positions = new ArrayList<>();
        positions.add(FIRST_AD_POSITION);
        positions.add(SECOND_AD_POSITION);
        if (MyApp.state == 1 && RxSPTool.getBoolean(requireActivity(), SpConstant.INDEX_DIALOG));


    }

    private void initView() {
        grade = RxSPTool.getString(requireActivity(), SpConstant.SELECT_GRADE);
        subject = RxSPTool.getString(requireActivity(), SpConstant.SELECT_SUBJECT);
        part = RxSPTool.getString(requireActivity(), SpConstant.SELECT_PART);
        version = RxSPTool.getString(requireActivity(), SpConstant.SELECT_VERSION);

        if (!TextUtils.isEmpty(grade)) tvGrade.setText(grade);
        if (!TextUtils.isEmpty(subject)) tvSubject.setText(subject);
        if (!TextUtils.isEmpty(part)) tvPart.setText(part);
        if (!TextUtils.isEmpty(version)) tvVersion.setText(version);
        getData();
    }

    private void initListener() {
        itemAdapter.setOnLoadMoreListener(this::getData, searchRecyclerView);
        itemAdapter.setOnItemClickListener((adapter, view, position) -> {
            BookInfo bookInfo = itemAdapter.getItem(position);
            if (bookInfo != null) {
                AnswerDetailActivity.startActivity(getActivity(), bookInfo.getName(), bookInfo.getBookId());
            }

        });

        itemAdapter.setOnItemChildClickListener((adapter, view, position) -> {

            textView = (TextView) view;
            if (UserInfoHelper.isLogin()) {
                mPresenter.favoriteAnswer((BookInfo) adapter.getItem(position));
            } else {
                mPresenter.saveBook((BookInfo) adapter.getItem(position));
            }
        });
        RxView.clicks(llGrade).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> showPopWindow(getString(R.string.grade), ivGrade, tvGrade));
        RxView.clicks(llSubject).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> showPopWindow(getString(R.string.subject), ivSubject, tvSubject));
        RxView.clicks(llPart).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showPopWindow(getString(R.string.part), ivPart, tvPart);

            }
        });
        RxView.clicks(llVersion).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> showPopWindow(getString(R.string.version), ivVersion, tvVersion));

        RxView.clicks(tvFeedback).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            if (!UserInfoHelper.isGoToLogin(getActivity())) {
                Intent intent = new Intent(getActivity(), UploadBookActivity.class);
                startActivity(intent);
            }
        });
        searchRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (showBottom) {

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        rlFeedback.setVisibility(View.VISIBLE);
                    } else {
                        rlFeedback.setVisibility(View.GONE);
                    }
                }

            }
        });

    }



    private void showPopWindow(String name, final ImageView iv, final TextView tv) {
        final FilterPopWindow popWindow = new FilterPopWindow(getActivity(), name);
        popWindow.showAsDropDown(llTopGuide);
        iv.setImageResource(R.mipmap.search_up);
        popWindow.setOnDismissListener(() -> iv.setImageResource(R.mipmap.search_down));
        popWindow.setOnPopClickListener(popName -> {
            popWindow.dismiss();
            tv.setText(popName);
            grade = tvGrade.getText().toString();
            subject = tvSubject.getText().toString();
            part = tvPart.getText().toString();
            version = tvVersion.getText().toString();
            page = 1;
//            code = "";
//            mName = "";
            getData();

        });

    }

    private void initAdapter() {
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemAdapter = new SearchResultItemAdapter(null, mAdViewPositionMap);
        searchRecyclerView.setAdapter(itemAdapter);

    }

    @Override
    public void showConditionList(List<String> data) {
    }

    @Override
    public void showBookInfoList(List<BookInfo> lists) {
        if (page == 1) {
            itemAdapter.setNewData(lists);
        } else {
            itemAdapter.addData(lists);
        }

        if (lists.size() > 0 && lists.size() == LIMIT) {
            page++;
            itemAdapter.loadMoreComplete();
        } else {
            itemAdapter.loadMoreEnd();
        }

    }

    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.COLLECT)
            })
    public void reloadData(String flag) {
        if (TextUtils.equals("collect", flag)) {
//            LogUtil.msg("flag  " + flag);
            page = 1;
            getData();
        }
    }

    @Override
    public void showFavoriteResult(boolean isCollect) {
        textView.setBackgroundResource(isCollect ? R.drawable.book_collect_gray_bg : R.drawable.book_collect_red_bg);
        textView.setText(isCollect ? "已收藏" : "收藏");
    }

    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(searchRecyclerView, v -> getData());
    }

    @Override
    public void showNoData() {
        stateView.showNoData(searchRecyclerView);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(searchRecyclerView);
    }

    private void getData() {
//        if (!TextUtils.isEmpty(code)) {
//            mPresenter.getBookList(page, LIMIT, "", code, "", "", "", "", "", "", "", "", "", "");
//        } else if (!TextUtils.isEmpty(mName)) {
//            mPresenter.getBookList(page, LIMIT, mName, "", "", "", "", "", "", "", "", "", "", "");
//        } else {
//            mPresenter.getBookList(page, LIMIT, "", "", "", grade, "", part, "", version, "", subject, "", "");
//        }
        mPresenter.getBookList(page, LIMIT, mName, code, "", grade, "", part, "", version, "", subject, "", "");
        loadAd();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxSPTool.putString(requireActivity(), SpConstant.SELECT_GRADE, grade);
        RxSPTool.putString(requireActivity(), SpConstant.SELECT_SUBJECT, subject);
        RxSPTool.putString(requireActivity(), SpConstant.SELECT_PART, part);
        RxSPTool.putString(requireActivity(), SpConstant.SELECT_VERSION, version);
    }



    //=================start===================信息流=====================================================================================
    private GMNativeAd gmNativeAd;
    private ViewGroup container;
    //step1:创建GMUnifiedNativeAd对象，传递Context对象和广告位ID即mAdUnitId
    GMUnifiedNativeAd mTTAdNative;
    //....省略其他代码  具体详见demo
    /**
     * config回调
     */
    private GMSettingConfigCallback mSettingConfigCallback = new GMSettingConfigCallback() {
        @Override
        public void configLoad() {
            Log.d("ccc", "load ad 在config 回调中加载广告");
            loadListAd(); //执行广告加载
        }
    };

    /**
     * 加载feed广告
     */
    private void loadExpressAd() {
        /**
         * 判断当前是否存在config 配置 ，如果存在直接加载广告 ，如果不存在则注册config加载回调
         */
        if (GMMediationAdSdk.configLoadSuccess()) {
            Log.d("ccc", "load ad 当前config配置存在，直接加载广告");
            loadListAd();
        } else {
            Log.d("ccc", "load ad 当前config配置不存在，正在请求config配置....");
            GMMediationAdSdk.registerConfigCallback(mSettingConfigCallback); //不能使用内部类，否则在ondestory中无法移除该回调
        }
    }


    public void loadListAd() {
        mTTAdNative = new GMUnifiedNativeAd(getActivity(), "102287912");//模板视频
        // 针对Gdt Native自渲染广告，可以自定义gdt logo的布局参数。该参数可选,非必须。
        FrameLayout.LayoutParams gdtNativeAdLogoParams =
                new FrameLayout.LayoutParams(
                        UIUtils.dip2px(getActivity().getApplicationContext(), 40),
                        UIUtils.dip2px(getActivity().getApplicationContext(), 13),
                        Gravity.RIGHT | Gravity.TOP); // 例如，放在右上角


        GMAdSlotGDTOption.Builder adSlotNativeBuilder = GMAdOptionUtil.getGMAdSlotGDTOption()
                .setNativeAdLogoParams(gdtNativeAdLogoParams);


        /**
         * 创建feed广告请求类型参数GMAdSlotNative,具体参数含义参考文档
         * 备注
         * 1: 如果是信息流自渲染广告，设置广告图片期望的图片宽高 ，不能为0
         * 2:如果是信息流模板广告，宽度设置为希望的宽度，高度设置为0(0为高度选择自适应参数)
         */
        GMAdSlotNative adSlotNative = new GMAdSlotNative.Builder()
                .setGMAdSlotBaiduOption(GMAdOptionUtil.getGMAdSlotBaiduOption().build())//百度相关的配置
                .setGMAdSlotGDTOption(adSlotNativeBuilder.build())//gdt相关的配置
                .setGMAdSlotBaiduOption(GMAdOptionUtil.getGMAdSlotBaiduOption().build())
                .setAdmobNativeAdOptions(GMAdOptionUtil.getAdmobNativeAdOptions())//admob相关配置
                .setAdStyleType(AdSlot.TYPE_EXPRESS_AD)//表示请求的模板广告还是原生广告，com.bytedance.msdk.api.AdSlot.TYPE_EXPRESS_AD：模板广告 ； com.bytedance.msdk.api.AdSlot.TYPE_NATIVE_AD：原生广告
                // 备注
                // 1:如果是信息流自渲染广告，设置广告图片期望的图片宽高 ，不能为0
                // 2:如果是信息流模板广告，宽度设置为希望的宽度，高度设置为0(0为高度选择自适应参数)
                .setImageAdSize((int) UIUtils.getScreenWidthDp(getActivity().getApplicationContext())-5, 0)// 必选参数 单位dp ，详情见上面备注解释
                .setAdCount(3)//请求广告数量为1到3条
                .build();


        //请求广告，调用feed广告异步请求接口，加载到广告后，拿到广告素材自定义渲染
        /**
         * 注：每次加载信息流广告的时候需要新建一个GMUnifiedNativeAd，否则可能会出现广告填充问题
         * (例如：mTTAdNative = new GMUnifiedNativeAd(this, mAdUnitId);）
         */
        mTTAdNative.loadAd(adSlotNative, new GMNativeAdLoadCallback() {
            @Override
            public void onAdLoaded(List<GMNativeAd> ads) {
                /**
                 * 获取已经加载的clientBidding ，多阶底价广告的相关信息
                 */
                if (ads == null || ads.isEmpty()) {
                    Log.d("ccc", "on FeedAdLoaded: ad is null!");
                    return;
                }

                //总广告数量
                int adCount = ads.size();
                if (adCount > 0) {
                   if (itemAdapter!=null){
                       itemAdapter.setExData(ads);
                       itemAdapter.notifyDataSetChanged();
                   }
                }
                // 获取本次waterfall加载中，加载失败的adn错误信息。
                if (mTTAdNative != null)
                    Log.d("ccc", "feed adLoadInfos: " + mTTAdNative.getAdLoadInfoList().toString());
            }

            @Override
            public void onAdLoadedFail(AdError adError) {

            }
        });
    }

    //=================end===================信息流=====================================================================================*/



}

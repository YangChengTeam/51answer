package com.yc.ac.index.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
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
import com.yc.ac.pay.PaySuccessInfo;
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
import yc.com.base.UIUtils;
import yc.com.tencent_adv.OnAdvStateListener;
import yc.com.toutiao_adv.TTAdDispatchManager;
import yc.com.toutiao_adv.TTAdType;

/**
 * Created by wanglin  on 2018/3/10 10:09.
 */

public class SearchFragment extends BaseFragment<BookConditionPresenter> implements BookConditionContract.View, OnAdvStateListener, yc.com.toutiao_adv.OnAdvStateListener {


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
    }

    private void loadAd() {
        List<Integer> positions = new ArrayList<>();
        positions.add(FIRST_AD_POSITION);
        positions.add(SECOND_AD_POSITION);
        if (MyApp.state == 1 && RxSPTool.getBoolean(requireActivity(), SpConstant.INDEX_DIALOG))
            TTAdDispatchManager.getManager().init(getActivity(), TTAdType.NATIVE_EXPRESS, null, Config.toutiao_native_id, AD_COUNT, positions, null, 0, null, 0, this);

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
        TTAdDispatchManager.getManager().onDestroy();
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onDismiss(long delayTime) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onNativeExpressDismiss(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onNativeExpressShow(Map<NativeExpressADView, Integer> mDatas) {

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

        for (Map.Entry<TTNativeExpressAd, Integer> nativeExpressADView : mDatas.entrySet()) {

            mAdViewPositionMap.put(nativeExpressADView.getKey(), nativeExpressADView.getValue());
            BookInfo bookInfo = new BookInfo();
            bookInfo.setItemType(BookInfo.ADV);
            bookInfo.setView(nativeExpressADView.getKey());

            itemAdapter.addADViewToPosition(nativeExpressADView.getValue(), bookInfo);
        }
    }

    @Override
    public void onNativeExpressDismiss(TTNativeExpressAd ad) {
        if (itemAdapter != null) {
            int removedPosition = mAdViewPositionMap.get(ad);
            itemAdapter.removeADView(removedPosition, ad);
        }
    }

    @Override
    public void onRewardVideoComplete() {

    }

    @Override
    public void loadRewardVideoSuccess() {

    }


}

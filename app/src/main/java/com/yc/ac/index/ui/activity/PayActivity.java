package com.yc.ac.index.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.ac.R;
import com.yc.ac.constant.BusAction;
import com.yc.ac.constant.HttpStatus;
import com.yc.ac.index.model.bean.PayItemInfo;
import com.yc.ac.index.ui.adapter.PayItemAdapter;
import com.yc.ac.pay.AlipayImpi;
import com.yc.ac.pay.PayInfo;
import com.yc.ac.pay.PayResultInfo;
import com.yc.ac.pay.PayResultListener;
import com.yc.ac.pay.PaySuccessInfo;
import com.yc.ac.pay.WxPayImpi;
import com.yc.ac.setting.contract.PayContract;
import com.yc.ac.setting.model.bean.UserInfo;
import com.yc.ac.setting.model.engine.OrderParamsInfo;
import com.yc.ac.setting.model.engine.WxPayInfo;
import com.yc.ac.setting.presenter.PayPresenter;
import com.yc.ac.utils.EngineUtils;
import com.yc.ac.utils.ItemDecorationHelper;
import com.yc.ac.utils.UserInfoHelper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Subscriber;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.UIUtils;

public class PayActivity extends BaseActivity<PayPresenter> implements PayContract.View, PayResultListener {
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;
    @BindView(R.id.tv_ok)
    TextView tvOk;

    @BindView(R.id.iv_banner)
    ImageView ivBanner;
    @BindView(R.id.tv_count_down)
    TextView tvCountDown;
    @BindView(R.id.recyclerView_item)
    RecyclerView recyclerViewItem;
    @BindView(R.id.tv_tint)
    TextView tvTint;
    @BindView(R.id.wx_pay)
    ConstraintLayout wxPay;
    @BindView(R.id.ali_pay)
    ConstraintLayout aliPay;
    @BindView(R.id.tv_pay)
    TextView tvPay;

    @BindView(R.id.tv_question)
    TextView tvQuestion;

    @BindView(R.id.iv_wx_sel)
    ImageView ivWxSel;
    @BindView(R.id.iv_ali_sel)
    ImageView ivAliSel;
    @BindView(R.id.tv_count_down_value)
    TextView tvCountDownValue;
    private PayItemAdapter payItemAdapter;
    private AlipayImpi alipayImpi;
    private WxPayImpi wxPayImpi;

    public static final String ALI_PAY = "ali_pay";
    public static final String WX_PAY = "wx_pay";
    private String payway = WX_PAY;

    private PayItemInfo payItemInfo;
    private String articleId;

    public static void startActivity(Context context, String articleId) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra("articleId", articleId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    public void init() {
        commonTvTitle.setText("充值购买");
        wxPay.setSelected(true);

        articleId = getIntent().getStringExtra("articleId");
        mPresenter = new PayPresenter(this, this);
        initRecyclerView();
        initListener();
        initData();
//        countDown();
    }

    private void initListener() {
        ivBack.setOnClickListener(v -> finish());
        payItemAdapter.setOnItemClickListener((adapter, view, position) -> payItemAdapter.setSelect(position));


        RxView.clicks(wxPay).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                aliPay.setSelected(false);
                wxPay.setSelected(true);
                ivAliSel.setVisibility(View.GONE);
                ivWxSel.setVisibility(View.VISIBLE);
                payway = WX_PAY;
            }
        });

        RxView.clicks(aliPay).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            aliPay.setSelected(true);
            wxPay.setSelected(false);
            ivAliSel.setVisibility(View.VISIBLE);
            ivWxSel.setVisibility(View.GONE);
            payway = ALI_PAY;
        });

        RxView.clicks(tvPay).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            if (!UserInfoHelper.isGoToLogin(PayActivity.this)) {
                if (TextUtils.equals(ALI_PAY, payway)) {
                    mPresenter.aliPay(UserInfoHelper.getUId(), payItemInfo.getId() + "", "0", articleId);
                } else {
                    mPresenter.wxPay(UserInfoHelper.getUId(), payItemInfo.getId() + "", "0", articleId);
                }
            }
        });

    }

    private void initRecyclerView() {
        payItemAdapter = new PayItemAdapter(null);
        recyclerViewItem.setAdapter(payItemAdapter);
        recyclerViewItem.addItemDecoration(new ItemDecorationHelper(10));
    }

    private void initData() {

    }

    private void aliPay(String orderInfo) {
        if (alipayImpi == null) {
            alipayImpi = new AlipayImpi(this, this);
        }
        alipayImpi.pay(orderInfo);
    }

    private void wxPay(OrderParamsInfo paramsInfo) {
        if (wxPayImpi == null) {
            wxPayImpi = new WxPayImpi(this);
        }
        wxPayImpi.pay(paramsInfo);
    }

    private void countDown() {
        CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
//                Log.e("TAG", "onTick: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                Log.e("TAG", "onFinish: ");
            }
        };
        countDownTimer.start();
    }

    @Override
    public void showPayItemInfos(List<PayItemInfo> payItemInfos) {
        if (payItemInfos != null && payItemInfos.size() > 0) {
            payItemInfo = payItemInfos.get(0);
        }
        payItemAdapter.setNewData(payItemInfos);
    }

    @Override
    public void showAliPayInfo(PayInfo data) {
        if (data != null) {
            aliPay(data.getOrderInfo());
        }
    }

    @Override
    public void showWxPayInfo(WxPayInfo data) {
        if (data != null) {
            wxPay(data.getOrderInfo());
        }
    }

    @Override
    public void paySuccess(String result) {

        UserInfo userInfo = UserInfoHelper.getUserInfo();
        userInfo.setVip(1);
        UserInfoHelper.saveUserInfo(userInfo);
        getUserInfo();

//        RxBus.get().post(BusAction.PAY_SUCCESS, new PaySuccessInfo(result));
        finish();
    }

    private void getUserInfo() {
        EngineUtils.getUserInfo(this, UserInfoHelper.getToken()).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<UserInfo> userInfoResultInfo) {
                if (userInfoResultInfo != null) {
                    if (userInfoResultInfo.getCode() == HttpConfig.STATUS_OK && userInfoResultInfo.getData() != null) {
                        UserInfoHelper.saveUserInfo(userInfoResultInfo.getData());
                    } else if (userInfoResultInfo.getCode() == HttpStatus.TOKEN_EXPIRED) {
                        UserInfoHelper.saveUserInfo(null);
                        UserInfoHelper.setToken("");
                    }

                }
            }
        });
    }

    @Override
    public void payFailure(String errMsg) {

    }

    @Override
    public void payCancel() {

    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.WX_PAY_SUCCESS)
            }
    )
    public void payNotification(PayResultInfo payResultInfo) {
        int code = payResultInfo.getCode();
        String message = payResultInfo.getMessage();
        switch (code) {
            case 0://支付成功
                paySuccess(message);
                break;
            case -1://错误
                payFailure(message);
                break;
            case -2://用户取消
                payCancel();
                break;
        }
    }


}

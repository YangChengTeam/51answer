package com.yc.answer.index.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.vondear.rxtools.RxSPTool;
import com.yc.answer.R;
import com.yc.answer.base.Constant;
import com.yc.answer.index.model.bean.AdvInfo;
import com.yc.answer.index.model.bean.SlideInfo;
import com.yc.answer.index.ui.widget.CommonWebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import yc.com.base.BaseFragment;

/**
 * Created by wanglin  on 2019/1/24 16:29.
 */
public class MainAdvFragment extends BaseFragment {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.commonWebView)
    CommonWebView wvMain;

    private String url = "http://m.upkao.com/ssyy.html";
    private Handler mHandler;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_adv;
    }

    @Override
    public void init() {
        ivBack.setVisibility(View.GONE);
        commonTvTitle.setText(getString(R.string.main1vs1));
        initWebview();
    }

    private void initWebview() {

        mHandler = new Handler();
        progressBar.setMax(100);

        AdvInfo advInfo = JSON.parseObject(RxSPTool.getString(getActivity(), Constant.ADV_INFO), AdvInfo.class);
        if (null != advInfo) {
            url = advInfo.getH5page();
        }
//        url += "?time=" + System.currentTimeMillis();


//        wvMain.addJavascriptInterface(new JavascriptInterface(), "study");


//        wvMain.loadUrl("file:///android_asset/m/ssyy.html");
        wvMain.loadUrl(url);
        wvMain.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

            }

        });
        wvMain.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                final int progress = newProgress;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (progressBar.getVisibility() == ProgressBar.GONE) {
                            progressBar.setVisibility(ProgressBar.VISIBLE);
                        }
                        progressBar.setProgress(progress);
                        progressBar.postInvalidate();
                        if (progress == 100) {
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });


            }
        });

    }
}

package com.yc.answer.base;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.yc.answer.R;
import com.yc.answer.index.ui.widget.CommonWebView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/3/15 15:56.
 */

public class WebActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;
    @BindView(R.id.webView)
    CommonWebView webView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    //    private LoadingDialog loadingDialog;
    private String url;

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void init() {
        url = getIntent().getStringExtra("url");
//        loadingDialog = new LoadingDialog(this);
        commonTvTitle.setText("");
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        initWebView(url);
    }

    private boolean isGoback;

    private void initWebView(String url) {


        progressBar.setMax(100);
        webView.loadUrl(url);


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, final int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (!isGoback) {
                    if (mHandler == null)
                        mHandler = new Handler(Looper.getMainLooper());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (progressBar.getVisibility() == ProgressBar.GONE) {
                                progressBar.setVisibility(ProgressBar.VISIBLE);
                            }
                            progressBar.setProgress(newProgress);
                            progressBar.postInvalidate();
                            if (newProgress == 100) {
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
//                view.loadUrl(url);
                progressBar.setVisibility(View.GONE);
                isGoback = false;
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                try {
                    URL url1 = new URL(url);
                    String req = "/zk/\\d+.html";
                    Pattern compile = Pattern.compile(req);//http://m.upkao.com/zk/index.html
                    Matcher matcher = compile.matcher(url1.getPath());
                    if (matcher.matches()) {
//                        isShowLoading = true;
                        LogUtil.msg("TAG: " + url + "--" + url1.getPath());
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                view.loadUrl(url);
                return false;


            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!TextUtils.isEmpty(url) && url.startsWith("http://m.upkao.com/zk/")) {
            if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                webView.goBack();
                isGoback = true;
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);

    }
}

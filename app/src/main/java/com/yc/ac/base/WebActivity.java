package com.yc.ac.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.yc.ac.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/3/15 15:56.
 */

public class WebActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private String url;

    public static void startActivity(Context context, String url, String title) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void init() {
        url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");

        commonTvTitle.setText(title);
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

        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//        webView.addJavascriptInterface(new JavascriptInterface(), "HTML");

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存 //优先使用缓存:
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setBlockNetworkImage(false);//设置是否加载网络图片 true 为不加载 false 为加载

        webView.loadUrl(url);

        progressBar.setMax(100);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (!isGoback) {

                    progressBar.setProgress(newProgress);
                }

            }


        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {

                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
                isGoback = false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

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
        webView.setDownloadListener((url12, userAgent, contentDisposition, mimetype, contentLength) -> {
                    //在这里进行下载的处理。
                    // 如果你没有进行处理，一般APP就不会开始下载行为，在这里可以自己开启一个线程来下载
                    Log.i("download", "url: " + url12);
                    Log.i("download", "contentDisposition: " + contentDisposition);
                    Log.i("download", "mimetype: " + mimetype);

                    /**
                     * 通过系统下载apk
                     */
                    if (url12.endsWith(".apk") || contentDisposition.endsWith(".apk")) {
                        Uri uri = Uri.parse(url12);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                }
        );

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (!TextUtils.isEmpty(url) && url.startsWith("http://m.upkao.com/zk/")) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            isGoback = true;
            return true;
        }
//        }
        return super.onKeyDown(keyCode, event);

    }

}

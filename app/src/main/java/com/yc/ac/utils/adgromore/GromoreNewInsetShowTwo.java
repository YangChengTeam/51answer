package com.yc.ac.utils.adgromore;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.bytedance.msdk.api.AdError;
import com.bytedance.msdk.api.reward.RewardItem;
import com.bytedance.msdk.api.v2.GMAdConstant;
import com.bytedance.msdk.api.v2.GMMediationAdSdk;
import com.bytedance.msdk.api.v2.GMSettingConfigCallback;
import com.bytedance.msdk.api.v2.ad.interstitialFull.GMInterstitialFullAd;
import com.bytedance.msdk.api.v2.ad.interstitialFull.GMInterstitialFullAdListener;
import com.bytedance.msdk.api.v2.ad.interstitialFull.GMInterstitialFullAdLoadCallback;
import com.bytedance.msdk.api.v2.slot.GMAdOptionUtil;
import com.bytedance.msdk.api.v2.slot.GMAdSlotInterstitialFull;
import com.kk.utils.VUiKit;
import com.yc.ac.utils.CommonUtils;
import com.yc.ac.utils.DisplayUtil;


import java.util.HashMap;
import java.util.Map;


public class GromoreNewInsetShowTwo {
    private Activity context;
    private static GromoreNewInsetShowTwo instance;
    public static GromoreNewInsetShowTwo getInstance() {
        if (instance == null) {
            synchronized (GromoreNewInsetShowTwo.class) {
                if (instance == null) {
                    instance = new GromoreNewInsetShowTwo();
                }
            }
        }
        return instance;
    }

   private boolean insetIsShow;
    private int   loadAdCount;;
    private int loadAdCountCache;
    private int dpw;
    private int dph;
    public void setContexts(Context contexts){
        this.context = (Activity) contexts;
        int screenWidth = DisplayUtil.getScreenWidth(context);
        int w = (int) (screenWidth)*9/10;
        int h = w*3/2;
         dpw = DisplayUtil.px2dip(context, w);
         dph = DisplayUtil.px2dip(context, h);
    }
    //=================start===================插屏=====================================================================================
    private String isTxLoadAdSuccess="0";//0 默认状态  1：开始播放  2：拉去广告失败  3：拉去广告成功
    private GMInterstitialFullAd mInterstitialFullAd;
    private boolean insetIsLoadSuccess;
    public void  showInset(OnNewInsetAdShowCaback onInsetAdShowCabacks){
        this.onAdShowCaback=onInsetAdShowCabacks;
        isTxLoadAdSuccess="1";
        if (insetIsLoadSuccess && mInterstitialFullAd != null && mInterstitialFullAd.isReady()) {
            //设置监听器
            mInterstitialFullAd.setAdInterstitialFullListener(interstitialListener);
            mInterstitialFullAd.showAd(context);
        }else {
            loadInset();
        }
    }


    public void  loadInset(){
        if (GMMediationAdSdk.configLoadSuccess()) {
            loadInteractionAd();
        } else {
            GMMediationAdSdk.registerConfigCallback(mSettingConfigCallbackinset);
        }
    }


    private GMSettingConfigCallback mSettingConfigCallbackinset = new GMSettingConfigCallback() {
        @Override
        public void configLoad() {
            LogUtils.showAdLog("load 首页插屏在config 回调中加载广告");
            loadInteractionAd();
        }
    };



    private boolean isLoad;
    /**
     * 加载插屏广告
     */
    private  GMAdSlotInterstitialFull adSlotInterstitialFull;
    private      String ad_code;
    private void loadInteractionAd() {
        //Context 必须传activity
        /**
         * 注：每次加载插屏广告的时候需要新建一个TTInterstitialAd，否则可能会出现广告填充问题
         * （ 例如：mInterstitialAd = new TTInterstitialAd(this, adUnitId);）
         */
        if (CommonUtils.isDestory(context)){
            return;
        }
        if (isLoad){
            return;
        }
        isLoad=true;
        mInterstitialFullAd = new GMInterstitialFullAd(context, "102288437");

        Map<String, String> customData = new HashMap<>();
        customData.put(GMAdConstant.CUSTOM_DATA_KEY_PANGLE, "pangle media_extra");
        customData.put(GMAdConstant.CUSTOM_DATA_KEY_GDT, "gdt custom data");
        customData.put(GMAdConstant.CUSTOM_DATA_KEY_KS, "ks custom data");
        // 其他需要透传给adn的数据。

         adSlotInterstitialFull = new GMAdSlotInterstitialFull.Builder()
                .setGMAdSlotBaiduOption(GMAdOptionUtil.getGMAdSlotBaiduOption().build())
                .setGMAdSlotGDTOption(GMAdOptionUtil.getGMAdSlotGDTOption().build())
                .setImageAdSize(dpw, dph)  //设置宽高 （插全屏类型下_插屏广告使用）
                .setVolume(0.5f) //admob 声音配置，与setMuted配合使用
                .setUserID("user123")//用户id,必传参数 (插全屏类型下_全屏广告使用)
                .setCustomData(customData)
                .setRewardName("金币") //奖励的名称，仅支持gdt全屏广告
                .setRewardAmount(3)  //奖励的数量
                 .setUseSurfaceView(false)
                .setOrientation(GMAdConstant.VERTICAL)//必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL; (插全屏类型下_全屏广告使用)
                .build();


        //请求广告，调用插屏广告异步请求接口
        mInterstitialFullAd.loadAd(adSlotInterstitialFull, new GMInterstitialFullAdLoadCallback() {
            @Override
            public void onInterstitialFullLoadFail(@NonNull AdError adError) {
                if (onAdShowCaback!=null){
                    onAdShowCaback.onError();
                }
                isLoad=false;
                LogUtils.showAdLog("load首页插屏error"+ adError.code + ", errMsg: " + adError.message);
                insetIsLoadSuccess = false;
                loadAdCount++;
                if (loadAdCount<=3){
                    loadInset();
                }else {
                    isTxLoadAdSuccess="0";
                }
                // 获取本次waterfall加载中，加载失败的adn错误信息。
            }

            @Override
            public void onInterstitialFullAdLoad() {
                isLoad=false;
                loadAdCount=0;
                LogUtils.showAdLog("load首页插屏success");
                insetIsLoadSuccess = true;
                VUiKit.postDelayed(100, new Runnable() {
                    @Override
                    public void run() {
                        if (mInterstitialFullAd!=null){
                            boolean ready = mInterstitialFullAd.isReady();
                            if (!ready){
                                if (onAdShowCaback!=null){
                                    onAdShowCaback.onError();
                                }
                                loadAdCountCache++;
                                if (loadAdCountCache<=2){
                                    loadInteractionAd();
                                }else {
                                    isTxLoadAdSuccess="0";
                                }
                            }else {
                                if ("1".equals(isTxLoadAdSuccess)){
                                    if (!CommonUtils.isDestory(context)){
                                        isTxLoadAdSuccess="5";
                                        mInterstitialFullAd.setAdInterstitialFullListener(interstitialListener);
                                        mInterstitialFullAd.showAd(context);
                                    }
                                }
                            }
                        }
                    }
                });
            }

            @Override
            public void onInterstitialFullCached() {

            }

        });
    }

    GMInterstitialFullAdListener interstitialListener = new GMInterstitialFullAdListener() {

        @Override
        public void onInterstitialFullShow() {
            loadAdCount=0;
            loadAdCountCache=0;
            isTxLoadAdSuccess="3";

            insetIsShow=true;
            if (onAdShowCaback!=null){
                onAdShowCaback.onRewardedAdShow();
            }
        }

        @Override
        public void onInterstitialFullShowFail(@NonNull AdError adError) {
            if (onAdShowCaback!=null){
                onAdShowCaback.onError();
            }
            if ("1".equals(isTxLoadAdSuccess)||"5".equals(isTxLoadAdSuccess)){
                isTxLoadAdSuccess="2";
            }
            insetIsShow=false;
            // 开发者应该结合自己的广告加载、展示流程，在该回调里进行重新加载
            loadInset();
        }

        @Override
        public void onInterstitialFullClick() {

        }

        @Override
        public void onInterstitialFullClosed() {
            mInterstitialFullAd=null;
            loadInset();
            loadAdCount=0;
            if (onAdShowCaback!=null){
                onAdShowCaback.onRewardedAdClosed(false);
            }
        }

        @Override
        public void onVideoComplete() {

        }

        @Override
        public void onVideoError() {

        }

        @Override
        public void onSkippedVideo() {

        }

        /**
         * 当广告打开浮层时调用，如打开内置浏览器、内容展示浮层，一般发生在点击之后
         * 常常在onAdLeftApplication之前调用
         */
        @Override
        public void onAdOpened() {
        }

        /**
         * 此方法会在用户点击打开其他应用（例如 Google Play）时
         * 于 onAdOpened() 之后调用，从而在后台运行当前应用。
         */
        @Override
        public void onAdLeftApplication() {
        }

        @Override
        public void onRewardVerify(@NonNull RewardItem rewardItem) {

        }
    };
    //=================end===================插屏=====================================================================================

    private OnNewInsetAdShowCaback onAdShowCaback;
    public interface OnNewInsetAdShowCaback{
        void onRewardedAdShow();
        void onError();
        void onRewardClick();
        void onRewardedAdClosed(boolean isVideoClick);
    }
}

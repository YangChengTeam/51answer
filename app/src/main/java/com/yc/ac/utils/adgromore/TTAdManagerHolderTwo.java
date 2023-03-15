package com.yc.ac.utils.adgromore;

import android.content.Context;
import android.util.Log;

import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdSdk;


/**
 * 可以用一个单例来保存TTAdManager实例，在需要初始化sdk的时候调用
 */
public class TTAdManagerHolderTwo {

    private static boolean sInit;

    public static TTAdManager get() {
        if (!sInit) {

        }
        return TTAdSdk.getAdManager();
    }

    public static void init(Context context, String appId, String appName, boolean isDebug,InitAdCallback initCallback) {
        doInit(context, appId, appName, isDebug,initCallback);
    }

    //step1:接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
    private static void doInit(Context context, String appId, String appName, boolean isDebug,InitAdCallback initCallback) {
        if (!sInit) {
            TTAdSdk.init(context, buildConfig(context, appId, appName, isDebug), new TTAdSdk.InitCallback(){
                @Override
                public void success() {
                    Log.d("ccc", "--------------success: ");
                    sInit = true;
                    if (initCallback!=null){
                        initCallback.onSuccess();
                    }
                }

                @Override
                public void fail(int i, String s) {
                    Log.d("ccc", "--------------fail: ");
                    sInit=false;
                }
            });

        }
    }


    private static TTAdConfig buildConfig(Context context, String appId, String appName, boolean isDebug) {
        return new TTAdConfig.Builder()
                .appId(appId)
                .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                .appName(appName)
                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                .allowShowNotify(true) //是否允许sdk展示通知栏提示
                .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                .debug(isDebug) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI, TTAdConstant.NETWORK_STATE_3G) //允许直接下载的网络状态集合
                .supportMultiProcess(false)//是否支持多进程
                //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
                .build();
    }
}

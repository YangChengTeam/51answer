package com.yc.ac.utils.adgromore;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;

import com.bytedance.msdk.api.TTAdConfig;
import com.bytedance.msdk.api.TTAdConstant;
import com.bytedance.msdk.api.TTMediationAdSdk;
import com.bytedance.msdk.api.UserInfoForSegment;
import com.bytedance.msdk.api.v2.GMPrivacyConfig;

import java.util.HashMap;
import java.util.Map;


/**
 * 可以用一个单例来保存TTAdManager实例，在需要初始化sdk的时候调用
 */
public class TTAdManagerHolder {

    private static boolean sInit;

    public static void init(Context context, String s, String APPNAME, boolean b, InitAdCallback initAdCallback) {
        doInit(context);
    }

    public static void initUnitySdkBanner(Activity activity) {
        TTMediationAdSdk.initUnityForBanner(activity);
    }


    //step1:接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
    private static void doInit(Context context) {
        if (!sInit) {
            TTMediationAdSdk.initialize(context, buildConfig(context));
            sInit = true;
        }
    }

    private static TTAdConfig buildConfig(Context context) {
        UserInfoForSegment userInfo = new UserInfoForSegment();
        userInfo.setUserId("msdk demo");
        userInfo.setGender(UserInfoForSegment.GENDER_MALE);
        userInfo.setChannel("msdk channel");
        userInfo.setSubChannel("msdk sub channel");
        userInfo.setAge(999);
        userInfo.setUserValueGroup("msdk demo user value group");

        Map<String, String> customInfos = new HashMap<>();
        customInfos.put("aaaa", "test111");
        customInfos.put("bbbb", "test222");
        userInfo.setCustomInfos(customInfos);
        return new TTAdConfig.Builder()
                .appId("5330144") //必填 ，不能为空
                .appName("答题宝宝") //必填，不能为空
                .openAdnTest(false)//开启第三方ADN测试时需要设置为true，会每次重新拉去最新配置，release 包情况下必须关闭.默认false
                .isPanglePaid(false)//是否为费用户
                .setPublisherDid(getAndroidId(context)) //用户自定义device_id
                .openDebugLog(true) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .usePangleTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                .setPangleTitleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                .allowPangleShowNotify(true) //是否允许sdk展示通知栏提示
                .allowPangleShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                .setPangleDirectDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI, TTAdConstant.NETWORK_STATE_3G) //允许直接下载的网络状态集合
                .needPangleClearTaskReset()//特殊机型过滤，部分机型出现包解析失败问题（大部分是三星）。参数取android.os.Build.MODEL
                .setUserInfoForSegment(userInfo) // 设置流量分组的信息
//                .customController(new TTCustomController() {}) // 该函数已经废弃，请不要使用。使用setPrivacyConfig进行权限设置
                .setPrivacyConfig(new GMPrivacyConfig() {
                    // 重写函数返回值设置需要设置的权限开关，不重写的采用默认值
                    @Override
                    public boolean isCanUsePhoneState() {
                        return true;
                    }
                })
                .build();
    }

    public static String getAndroidId(Context context) {
        String androidId = null;
        try {
            androidId = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return androidId;
    }

}

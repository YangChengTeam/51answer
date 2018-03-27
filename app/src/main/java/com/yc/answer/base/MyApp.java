package com.yc.answer.base;

import android.os.Build;
import android.support.multidex.MultiDexApplication;

import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.share.UMShareImpl;
import com.tencent.bugly.Bugly;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.vondear.rxtools.RxTool;
import com.yc.answer.utils.UserInfoHelper;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by wanglin  on 2018/2/27 11:15.
 */

public class MyApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Observable.just("").subscribeOn(Schedulers.io()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                init();
            }
        });

        Bugly.init(getApplicationContext(), "af5788360b", false);
    }


    private void init() {
        RxTool.init(this);
        UMGameAgent.setDebugMode(false);
        UMGameAgent.init(this);
        UMGameAgent.setPlayerLevel(1);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        PlatformConfig.setWeixin("wxf80e368f5707d8ab", "a65d2fddc3571674ea037db01223e845");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
//        Config.DEBUG = true;
        UMShareAPI.get(this);


        //全局信息初始化

        UserInfoHelper.login(this);
        GoagalInfo.get().init(this);

        //设置http默认参数
        String agent_id = "1";
        Map<String, String> params = new HashMap<>();
        if (GoagalInfo.get().channelInfo != null && GoagalInfo.get().channelInfo.agent_id != null) {
            params.put("from_id", GoagalInfo.get().channelInfo.from_id + "");
            params.put("author", GoagalInfo.get().channelInfo.author + "");
            agent_id = GoagalInfo.get().channelInfo.agent_id;
        }
        params.put("agent_id", agent_id);
        params.put("imeil", GoagalInfo.get().uuid);
        String sv = getSV();
        params.put("sv", sv);
        params.put("device_type", "2");
        if (GoagalInfo.get().packageInfo != null) {
            params.put("app_version", GoagalInfo.get().packageInfo.versionCode + "");
        }
        HttpConfig.setDefaultParams(params);

    }

    public static String getSV() {
        return Build.MODEL.contains(Build.BRAND) ? Build.MODEL + " " + Build.VERSION.RELEASE : Build.BRAND + " " + Build.MODEL + " " + Build.VERSION.RELEASE;
    }
}

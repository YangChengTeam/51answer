package com.yc.answer.base;

import android.graphics.Color;
import android.os.Build;
import android.support.multidex.MultiDexApplication;

import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.tencent.bugly.Bugly;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.vondear.rxtools.RxTool;
import com.yc.answer.R;
import com.yc.answer.index.listener.GlidePauseOnScrollListener;
import com.yc.answer.index.model.bean.DaoMaster;
import com.yc.answer.index.model.bean.DaoSession;
import com.yc.answer.index.ui.widget.GlideImageLoader;
import com.yc.answer.utils.UserInfoHelper;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.HashMap;
import java.util.Map;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by wanglin  on 2018/2/27 11:15.
 */

public class MyApp extends MultiDexApplication {

    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        Observable.just("").subscribeOn(Schedulers.io()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                init();
                initGalleryFinal();
            }
        });

        Bugly.init(getApplicationContext(), "af5788360b", false);


        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "answer-db", null);
        Database db = helper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;


    }


    public static DaoSession getDaoSession() {
        return daoSession;
    }

    private void init() {
        RxTool.init(this);
        UMGameAgent.setDebugMode(false);
        UMGameAgent.init(this);
        UMGameAgent.setPlayerLevel(1);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        PlatformConfig.setWeixin("wxf80e368f5707d8ab", "133811fd0a1c77d289731fdbd56a786b");
        PlatformConfig.setQQZone("1108013607", "2mun88jc6IlVjy1A");

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
        UserInfoHelper.getAdvInfo(this);

    }

    public static String getSV() {
        return Build.MODEL.contains(Build.BRAND) ? Build.MODEL + " " + Build.VERSION.RELEASE : Build.BRAND + " " + Build.MODEL + " " + Build.VERSION.RELEASE;
    }

    private void initGalleryFinal() {

        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.rgb(228, 50, 50))//标题栏背景颜色
                .setFabNornalColor(Color.rgb(228, 50, 50))//确定按钮Noimal状态
                .setFabPressedColor(Color.rgb(228, 60, 60))//确定按钮Pressed状态
                .setCheckNornalColor(Color.rgb(204, 204, 204))
                .setCheckSelectedColor(Color.rgb(228, 50, 50))//选择框选中颜色
                .setIconBack(R.mipmap.back)//返回按钮
                .build();
        //初始化图片选择器
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnablePreview(true)//是否开启预览功能
                .setEnableEdit(true)//开启编辑功能
                .setEnableCrop(true)//开启裁剪功能
                .setEnableCamera(true)//开启相机功能
                .setCropWidth(getResources().getDisplayMetrics().widthPixels)
                .setCropHeight(getResources().getDisplayMetrics().widthPixels)
                .build();

        GlideImageLoader imageloader = new GlideImageLoader();

        //配置imageloader
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageloader, theme)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(new GlidePauseOnScrollListener(false, true))
                .build();

        GalleryFinal.init(coreConfig);
        //设置主题
    }
}

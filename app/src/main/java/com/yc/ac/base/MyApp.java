package com.yc.ac.base;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;

import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.tencent.bugly.Bugly;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.vondear.rxtools.RxTool;
import com.yc.ac.R;
import com.yc.ac.index.listener.GlidePauseOnScrollListener;
import com.yc.ac.index.model.bean.AdStateInfo;
import com.yc.ac.index.model.bean.DaoMaster;
import com.yc.ac.index.model.bean.DaoSession;
import com.yc.ac.index.ui.widget.GlideImageLoader;
import com.yc.ac.utils.EngineUtils;
import com.yc.ac.utils.UserInfoHelper;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.HashMap;
import java.util.Map;

import androidx.multidex.MultiDexApplication;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import yc.com.toutiao_adv.TTAdManagerHolder;

/**
 * Created by wanglin  on 2018/2/27 11:15.
 */

public class MyApp extends MultiDexApplication {


    private static DaoSession daoSession;
    public static int state;

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

        TTAdManagerHolder.init(this, Config.toutiao_ad_id);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "answer-circle-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster dm = new DaoMaster(db);

        daoSession = dm.newSession();
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;


    }


    public static DaoSession getDaoSession() {
        return daoSession;
    }

    private void init() {
        RxTool.init(this);

        //友盟统计
//        UMGameAgent.setDebugMode(false);
//        UMGameAgent.init(this);
//        UMGameAgent.setPlayerLevel(1);
        UMConfigure.setLogEnabled(true);

        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);


        UMConfigure.init(this, "5c3ea2e5b465f59e50001540", "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");//58edcfeb310c93091c000be2 5965ee00734be40b580001a0
        //开启debug模式，方便定位错误，具体错误检查方式可以查看
        //http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        UMConfigure.setLogEnabled(false);


        PlatformConfig.setWeixin("wx622cbf19fcb00f29", "4aa67b15c55411749dddfbad0cd66798");
//        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
        PlatformConfig.setWXFileProvider("com.yc.ac.fileprovider");

        PlatformConfig.setQQZone("1108013607", "2mun88jc6IlVjy1A");
        PlatformConfig.setQQFileProvider("com.yc.ac.fileprovider");

        //初始化友盟SDK
        UMShareAPI.get(this);//初始化sd

//        Config.DEBUG = true;


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
                .setTakePhotoFolder(getExternalFilesDir(Environment.DIRECTORY_PICTURES))
                .setPauseOnScrollListener(new GlidePauseOnScrollListener(false, true))
                .build();

        GalleryFinal.init(coreConfig);
        //设置主题
    }


}

package com.yc.ac.base;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;

import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.tencent.bugly.Bugly;
import com.tencent.mmkv.MMKV;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.RxTool;
import com.yc.ac.R;
import com.yc.ac.constant.SpConstant;
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
        Observable.just("").subscribeOn(Schedulers.io()).subscribe(s -> {
            initGalleryFinal();
        });

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "answer-circle-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster dm = new DaoMaster(db);

        daoSession = dm.newSession();
//        QueryBuilder.LOG_SQL = true;
//        QueryBuilder.LOG_VALUES = true;


    }


    public static DaoSession getDaoSession() {
        return daoSession;
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

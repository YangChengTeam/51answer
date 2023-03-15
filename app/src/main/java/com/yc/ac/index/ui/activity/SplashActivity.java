package com.yc.ac.index.ui.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.LogUtil;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.tencent.bugly.Bugly;
import com.tencent.mmkv.MMKV;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.vondear.rxtools.RxDeviceTool;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.RxTool;
import com.yc.ac.R;
import com.yc.ac.base.Config;
import com.yc.ac.base.MainActivity;
import com.yc.ac.base.MyApp;
import com.yc.ac.constant.SpConstant;
import com.yc.ac.index.listener.GlidePauseOnScrollListener;
import com.yc.ac.index.model.bean.AdStateInfo;
import com.yc.ac.index.model.bean.DaoMaster;
import com.yc.ac.index.ui.fragment.PolicyTintFragment;
import com.yc.ac.index.ui.widget.GlideImageLoader;
import com.yc.ac.index.ui.widget.SelectGradeView;
import com.yc.ac.utils.EngineUtils;
import com.yc.ac.utils.UserInfoHelper;
import com.yc.ac.utils.adgromore.GMAdManagerHolder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

import butterknife.BindView;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import yc.com.base.BaseActivity;
import yc.com.base.StatusBarUtil;


/**
 * Created by wanglin  on 2018/3/15 14:11.
 */

public class SplashActivity extends BaseActivity {

    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.smallGradeView)
    SelectGradeView smallGradeView;
    @BindView(R.id.middleGradeView)
    SelectGradeView middleGradeView;
    @BindView(R.id.seniorGradeView)
    SelectGradeView seniorGradeView;
    @BindView(R.id.ll_select_grade)
    LinearLayout llSelectGrade;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.rl_select_grade)
    RelativeLayout rlSelectGrade;
    @BindView(R.id.splash_container)
    FrameLayout splashContainer;
    @BindView(R.id.skip_view)
    TextView skipView;

    private final int Time = 1000;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void init() {
        String agreement = RxSPTool.getString(SplashActivity.this, SpConstant.AGREE_MENT);
        if (!TextUtils.isEmpty(agreement)){
            initViews();
        }else {
            showAgreementDialog();
        }
    }

    public void initViews(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getApplicationContext(), "answer-circle-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster dm = new DaoMaster(db);
        MyApp.daoSession = dm.newSession();

        GMAdManagerHolder.init(getApplicationContext());
        Bugly.init(getApplicationContext(), "fa044f8f97", false);

     /*   TTAdManagerHolderTwo.init(getApplicationContext(), "5372600", Constant.APPNAME, false, new InitAdCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure() {

            }
        });*/

        initsdk();
        rlSelectGrade.setVisibility(View.GONE);
//        iv.setVisibility(View.VISIBLE);
        getAdState();
       // switchActivity(Time);
    }

    public void initsdk() {
        MMKV.initialize(this);
        initGalleryFinal();
        RxTool.init(getApplicationContext());

        //友盟统计
//        UMGameAgent.setDebugMode(false);
//        UMGameAgent.init(this);
//        UMGameAgent.setPlayerLevel(1);
        UMConfigure.setLogEnabled(true);

        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);

        UMConfigure.preInit(this, "5c3ea2e5b465f59e50001540", "umeng");
        if (RxSPTool.getBoolean(this, SpConstant.INDEX_DIALOG)) {
            UMConfigure.init(this, "5c3ea2e5b465f59e50001540", "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");//58edcfeb310c93091c000be2 5965ee00734be40b580001a0
        }
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
        CoreConfig coreConfig = new CoreConfig.Builder(getApplicationContext(), imageloader, theme)
                .setFunctionConfig(functionConfig)
                .setTakePhotoFolder(getExternalFilesDir(Environment.DIRECTORY_PICTURES))
                .setPauseOnScrollListener(new GlidePauseOnScrollListener(false, true))
                .build();

        GalleryFinal.init(coreConfig);
        //设置主题
    }

    public static String getSV() {
        return Build.MODEL.contains(Build.BRAND) ? Build.MODEL + " " + Build.VERSION.RELEASE : Build.BRAND + " " + Build.MODEL + " " + Build.VERSION.RELEASE;
    }


    protected void showAgreementDialog() {
        PolicyTintFragment policyTintFragment = new PolicyTintFragment();
        policyTintFragment.show(getSupportFragmentManager(), "");
        policyTintFragment.setOnGrantListener(new PolicyTintFragment.OnGrantListener() {
            @Override
            public void onAgree() {
                RxSPTool.putString(SplashActivity.this, SpConstant.AGREE_MENT,"agreement");
                initViews();
            }
        });
    }


    private void getAdState() {
        EngineUtils.getAdStateState(this).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultInfo<AdStateInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.msg("onError: " + e.getMessage());
                        switchActivity(Time);
                    }

                    @Override
                    public void onNext(ResultInfo<AdStateInfo> adStateInfoResultInfo) {
                        if (adStateInfoResultInfo != null && adStateInfoResultInfo.getCode() == HttpConfig.STATUS_OK) {
                            MyApp.state = adStateInfoResultInfo.getData().status;
                            if (MyApp.state == 1) {//打开广告
                              //  TTAdDispatchManager.getManager().init(SplashActivity.this, TTAdType.SPLASH, splashContainer, Config.toutiao_splash_id, 0, null, null, 0, null, 0, SplashActivity.this);
                            } else {

                            }
                            switchActivity(Time);
                        }
                    }
                });
    }

    private void switchActivity(long delayTime) {
        if (mHandler == null) {
            mHandler = new Handler(Objects.requireNonNull(Looper.myLooper()));
        }
        mHandler.postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, delayTime);
    }

    //是否是指定机型
    private boolean isAssignPhone() {
        return TextUtils.equals("huawei", Build.BRAND.toLowerCase()) || TextUtils.equals("honor", Build.BRAND.toLowerCase());
    }

    private void initListener() {
        RxView.clicks(tvSkip).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                switchActivity(Time);
            }
        });
    }

    private void initSelectView() {
        if (RxDeviceTool.getScreenHeight(this) >= 1920) {
            RelativeLayout.MarginLayoutParams params = (RelativeLayout.MarginLayoutParams) llSelectGrade.getLayoutParams();
            params.topMargin = RxImageTool.dip2px(RxDeviceTool.getScreenHeight(this) / 10);
            llSelectGrade.setLayoutParams(params);
        }

        final List<String> smallGrades = Arrays.asList(getResources().getStringArray(R.array.small_grade));
        smallGradeView.setContents(smallGrades);
        middleGradeView.setContents(Arrays.asList(getResources().getStringArray(R.array.middle_grade)));
        seniorGradeView.setContents(Arrays.asList(getResources().getStringArray(R.array.senior_grade)));

//        smallGradeView.post(new Runnable() {
//            @Override
//            public void run() {
//                smallGradeView.click(0);
//                RxSPTool.putString(SplashActivity.this, SpConstant.SELECT_GRADE, smallGrades.get(0));
//                switchActivity();
//            }
//        });
        setSelectState(smallGradeView);
        setSelectState(middleGradeView);
        setSelectState(seniorGradeView);

    }


    private void setSelectState(final SelectGradeView view) {
        view.setOnSelectGradeListener((position, data) -> {
            middleGradeView.clearSelect();
            seniorGradeView.clearSelect();
            smallGradeView.clearSelect();
            view.click(position);
            RxSPTool.putString(SplashActivity.this, SpConstant.SELECT_GRADE, data);
            switchActivity(Time);
        });

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTransparentForWindow(this);
    }




    @Override
    protected void onPause() {
        super.onPause();
//        AdvDispatchManager.getManager().onPause();
       // TTAdDispatchManager.getManager().onStop();
    }

    @Override
    protected void onResume() {
//        AdvDispatchManager.getManager().onResume();
        super.onResume();
     //   TTAdDispatchManager.getManager().onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }






}

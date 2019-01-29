package com.yc.ac.index.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.kk.utils.LogUtil;
import com.vondear.rxtools.RxDeviceTool;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.RxKeyboardTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxAutoCompleteTextView;
import com.yc.ac.R;
import com.yc.ac.base.MainActivity;
import com.yc.ac.constant.SpConstant;
import com.yc.ac.index.contract.SearchContract;
import com.yc.ac.index.presenter.SearchPresenter;
import com.yc.ac.index.ui.adapter.AutoCompleteAdapter;
import com.yc.ac.index.ui.fragment.SearchCodeFragment;
import com.yc.ac.index.ui.fragment.SearchConditionFragment;
import com.yc.ac.index.ui.fragment.SearchResultFragment;
import com.yc.ac.utils.ActivityScanHelper;
import com.yc.ac.utils.SearchHistoryHelper;
import com.yc.ac.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/3/9 20:08.
 */

public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchContract.View {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.et_search)
    AppCompatAutoCompleteTextView etSearch;
    @BindView(R.id.iv_scan)
    ImageView ivScan;
    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;
    @BindView(R.id.btn_search)
    TextView btnSearch;
    @BindView(R.id.container)
    FrameLayout container;


    private List<Fragment> fragmentList;
    private int page;
    private String subject;
    private String grade;
    private String part;
    private String code;
    private String name;
    private String version;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    private boolean isFoucusable = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void init() {

        if (getIntent() != null) {
            page = getIntent().getIntExtra("page", 0);
            name = getIntent().getStringExtra("text");

            subject = getIntent().getStringExtra("subject");
            grade = getIntent().getStringExtra("grade");
            part = getIntent().getStringExtra("part");
            code = getIntent().getStringExtra("code");
            version = getIntent().getStringExtra("version");
        }
        if (page == 2) {
            etSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
            etSearch.setHint("请输入书籍条形码");
        }

        mPresenter = new SearchPresenter(this, this);

        initFragment();

//        SpanUtils.setEditTextHintSize(etSearch);
        mLocationClient = new LocationClient(getApplicationContext());
        initLocation();

        mLocationClient.registerLocationListener(myListener);
        mLocationClient.start();

        initListener();
    }

    private void initFragment() {
        fragmentList = new ArrayList<>();
        SearchConditionFragment conditionFragment = new SearchConditionFragment();
        SearchResultFragment searchResultFragment = new SearchResultFragment();
        SearchCodeFragment searchCodeFragment = new SearchCodeFragment();
        if (!fragmentList.contains(conditionFragment)) {
            fragmentList.add(conditionFragment);
        }
        if (!fragmentList.contains(searchResultFragment))
            fragmentList.add(searchResultFragment);
        if (!fragmentList.contains(searchCodeFragment))
            fragmentList.add(searchCodeFragment);
        replaceFragment(page, name, subject, grade, part, code, version);
    }


    private void initListener() {
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        RxView.clicks(btnSearch).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                etSearch.dismissDropDown();
                String inputText = etSearch.getText().toString().trim();
                if (TextUtils.isEmpty(inputText)) {
                    ToastUtils.showCenterToast(SearchActivity.this, "请输入相关书籍名称");
                    return;
                }
                RxKeyboardTool.hideSoftInput(SearchActivity.this);

                search(inputText);

//                replaceFragment(1, inputText, subject, grade, part, code);

            }
        });

        RxView.clicks(ivScan).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {


                if (!RxSPTool.getBoolean(SearchActivity.this, SpConstant.IS_OPEN_SCAN)) {
                    startActivity(new Intent(SearchActivity.this, ScanTintActivity.class));
                    RxSPTool.putBoolean(SearchActivity.this, SpConstant.IS_OPEN_SCAN, true);
                } else {
                    ActivityScanHelper.startScanerCode(SearchActivity.this);
                }
                finish();
            }
        });
        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isFoucusable = hasFocus;
                if (hasFocus) {
                    mPresenter.searchTips(((AutoCompleteTextView) v).getText().toString().trim());
                }
            }
        });
    }

    private void search(String inputText) {

        Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
        if (page == 2) {
            intent.putExtra("code", inputText);
        } else {
            intent.putExtra("text", inputText);
            SearchHistoryHelper.saveHistoryList(inputText);
        }
        intent.putExtra("page", 1);
        startActivity(intent);
        finish();
    }


    public void replaceFragment(int position, String text, String subject, String grade, String part, String code, String version) {
        name = text;
        if (!TextUtils.isEmpty(name)) etSearch.setText(name);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment : fragmentList) {
            if (fragment.isAdded()) {
                ft.hide(fragment);
            }
        }
        Fragment fragment = fragmentList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("text", text);
        bundle.putString("subject", subject);
        bundle.putString("grade", grade);
        bundle.putString("part", part);
        bundle.putString("code", code);
        bundle.putString("version", version);

        fragment.setArguments(bundle);
        if (!fragment.isAdded()) {
            ft.add(R.id.container, fragment);
        } else {
            ft.show(fragment);
        }

        ft.commit();

    }


    private void initAutoTextView() {

        RxTextView.textChanges(etSearch)
                .debounce(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        if (isFoucusable) {
                            mPresenter.searchTips(charSequence.toString().trim());
                        }
                    }
                });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            initAutoTextView();
        }
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认gcj02
        //gcj02：国测局坐标；
        //bd09ll：百度经纬度坐标；
        //bd09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

        option.setScanSpan(10000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
        //可选，7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位

        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
    }

    @Override
    public void showSearchTips(List<String> data) {
        etSearch.setDropDownHorizontalOffset(-RxImageTool.dp2px(75));

//        etSearch.setDropDownWidth(RxDeviceTool.getScreenWidth(this) - RxImageTool.dip2px(btnSearch.getMeasuredWidth() + 30));
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//
//                android.R.layout.simple_dropdown_item_1line, list);

        AutoCompleteAdapter adapter = new AutoCompleteAdapter(this, data);
        etSearch.setAdapter(adapter);
        etSearch.showDropDown();
        etSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                search(etSearch.getText().toString().trim());
            }
        });

    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            LogUtil.msg("TAG: " + latitude + "---" + longitude);

            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
        }
    }
}

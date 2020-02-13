package com.yc.ac.index.ui.activity;


import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.vondear.rxtools.RxKeyboardTool;
import com.yc.ac.R;
import com.yc.ac.index.contract.SearchContract;
import com.yc.ac.index.presenter.SearchPresenter;
import com.yc.ac.index.ui.fragment.SearchCodeFragment;
import com.yc.ac.utils.ActivityScanHelper;
import com.yc.ac.utils.ToastUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/3/9 20:08.
 */

public class SearchCodeActivity extends BaseActivity<SearchPresenter> implements SearchContract.View {
    @BindView(R.id.iv_back)
    ImageView ivBack;


    @BindView(R.id.iv_scan)
    ImageView ivScan;
    @BindView(R.id.btn_search)
    TextView btnSearch;

    AppCompatAutoCompleteTextView etSearch;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void init() {
        etSearch = findViewById(R.id.et_search);

        etSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
        etSearch.setHint("请输入书籍条形码");

        SearchCodeFragment searchCodeFragment = new SearchCodeFragment();
        replaceFragment(searchCodeFragment);

        mPresenter = new SearchPresenter(this, this);

        initListener();
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

                String inputText = etSearch.getText().toString().trim();

                if (TextUtils.isEmpty(inputText)) {
                    ToastUtils.showCenterToast(SearchCodeActivity.this, "条形码不能为空");
                    return;
                }
                Pattern pattern = Pattern.compile("^978\\d{10}$");
                if (!pattern.matcher(inputText).matches()) {
                    ToastUtils.showCenterToast(SearchCodeActivity.this, "请输入正确的书籍条形码");
                    return;
                }

                RxKeyboardTool.hideSoftInput(SearchCodeActivity.this);

                search(inputText);

            }
        });

        RxView.clicks(ivScan).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {


                ActivityScanHelper.startScanerCode(SearchCodeActivity.this);

                finish();
            }
        });
    }

    private void search(String inputText) {

        Intent intent = new Intent(SearchCodeActivity.this, SearchActivityNew.class);
        intent.putExtra("code", inputText);
        startActivity(intent);
        finish();
    }


    public void replaceFragment(Fragment fragment) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        ft.replace(R.id.container, fragment);

        ft.commit();

    }


    @Override
    public void showSearchTips(List<String> data) {

    }


}

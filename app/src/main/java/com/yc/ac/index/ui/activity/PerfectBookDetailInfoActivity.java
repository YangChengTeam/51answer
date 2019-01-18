package com.yc.ac.index.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.vondear.rxtools.RxSPTool;
import com.yc.ac.R;
import com.yc.ac.constant.SpConstant;
import com.yc.ac.index.ui.fragment.BookGradeFragment;
import com.yc.ac.index.ui.fragment.BookNameFragment;
import com.yc.ac.index.ui.fragment.BookSubjectFragment;
import com.yc.ac.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import rx.functions.Func1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/4/20 17:32.
 */

public class PerfectBookDetailInfoActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;
    @BindView(R.id.tv_ok)
    TextView tvOk;

    private List<Fragment> fragmentList;

    private int position;
    private String result;
    private int currentPos;


    @Override
    public int getLayoutId() {
        return R.layout.activity_perfect_book_detail_info;
    }

    @Override
    public void init() {
        tvOk.setVisibility(View.VISIBLE);
        tvOk.setTextColor(ContextCompat.getColor(this, R.color.gray_aaa));
        position = getIntent().getIntExtra("pos", 0);
        fragmentList = new ArrayList<>();
        fragmentList.add(new BookNameFragment());
        fragmentList.add(new BookGradeFragment());
        fragmentList.add(new BookSubjectFragment());
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        commonTvTitle.setText(position == 0 ? "课本名称" : position == 1 ? "选择年级" : "选择科目");

        replaceFragment(position);
        initListener();
    }

    private void initListener() {
        RxView.clicks(tvOk).filter(new Func1<Void, Boolean>() {
            @Override
            public Boolean call(Void aVoid) {

                if (TextUtils.isEmpty(result)) {
                    ToastUtils.showCenterToast(PerfectBookDetailInfoActivity.this, position == 0 ? "书本名称不能为空" : position == 1 ? "年级不能为空" : "科目不能为空");
                }
                return !TextUtils.isEmpty(result);
            }
        }).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                savePos();
                Intent intent = getIntent();
                intent.putExtra("result", result);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public void replaceFragment(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragmentList.get(position));
        ft.commit();
    }

    public void transmitData(int pos, String data) {
        if (!TextUtils.isEmpty(data)) {
            tvOk.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            tvOk.setTextColor(ContextCompat.getColor(this, R.color.gray_aaa));
        }
        result = data;
        currentPos = pos;
    }

    private void savePos() {
        if (position == 0) {
            RxSPTool.putString(this, SpConstant.BOOK_NAME, result);
        } else if (position == 2) {
            String saveData = result + "-" + currentPos;
            RxSPTool.putString(this, SpConstant.BOOK_SUBJECT, saveData);
        }
    }


}

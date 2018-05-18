package com.yc.answer.index.ui.widget;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.answer.R;
import com.yc.answer.constant.SpConstant;
import com.yc.answer.index.model.bean.VersionDetailInfo;
import com.yc.answer.index.model.bean.VersionInfo;
import com.yc.answer.index.ui.activity.SearchActivity;
import com.yc.answer.index.ui.adapter.FilterItemDetailAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BasePopwindow;
import yc.com.base.CommonInfoHelper;


/**
 * Created by wanglin  on 2018/3/8 15:53.
 */

public class FilterPopwindow extends BasePopwindow {


    @BindView(R.id.subject_recyclerView)
    RecyclerView subjectRecyclerView;
    @BindView(R.id.grade_recyclerView)
    RecyclerView gradeRecyclerView;
    @BindView(R.id.part_recyclerView)
    RecyclerView partRecyclerView;
    @BindView(R.id.tv_reset)
    TextView tvReset;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.version_recyclerView)
    RecyclerView versionRecyclerView;
    private FilterItemDetailAdapter subjectFilterItemAdapter;
    private FilterItemDetailAdapter patrFilterItemAdapter;
    private FilterItemDetailAdapter gradeFilterItemAdapter;
    private VersionDetailInfo subjectDetailInfo;
    private VersionDetailInfo gradeDetailInfo;
    private VersionDetailInfo patrDetailInfo;
    private VersionDetailInfo versionDetailInfo;
    private FilterItemDetailAdapter versionFilterItemAdapter;


    public FilterPopwindow(Activity context) {
        super(context);
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_filter_view;
    }

    @Override
    public void init() {

        setOutsideTouchable(true);
        subjectRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        gradeRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        partRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        versionRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        CommonInfoHelper.getO(mContext, SpConstant.INDEX_VERSION, new TypeReference<VersionInfo>() {
        }.getType(), new CommonInfoHelper.onParseListener<VersionInfo>() {


            @Override
            public void onParse(VersionInfo versionInfo) {

//                filterInfos.add(new FilterInfo("科目", versionInfo.getSubject()));
//                filterInfos.add(new FilterInfo("年级", versionInfo.getGrade()));
//                filterInfos.add(new FilterInfo("上下册", versionInfo.getPart_type()));
//                FilterItemAdapter filterItemAdapter = new FilterItemAdapter(filterInfos);


                List<VersionDetailInfo> subjectList = createNewList(versionInfo.getSubject());
                List<VersionDetailInfo> gradeList = createNewList(versionInfo.getGrade());
                List<VersionDetailInfo> partList = createNewList(versionInfo.getPart_type());
                List<VersionDetailInfo> versionList = createNewList(versionInfo.getVersion());
                subjectDetailInfo = subjectList.get(0);
                gradeDetailInfo = gradeList.get(0);
                patrDetailInfo = partList.get(0);
                versionDetailInfo = versionList.get(0);
                subjectFilterItemAdapter = new FilterItemDetailAdapter(subjectList, "");

                gradeFilterItemAdapter = new FilterItemDetailAdapter(gradeList, "");

                patrFilterItemAdapter = new FilterItemDetailAdapter(partList, "");

                versionFilterItemAdapter = new FilterItemDetailAdapter(versionList, "");

                subjectRecyclerView.setAdapter(subjectFilterItemAdapter);
                gradeRecyclerView.setAdapter(gradeFilterItemAdapter);
                partRecyclerView.setAdapter(patrFilterItemAdapter);
                versionRecyclerView.setAdapter(versionFilterItemAdapter);
                subjectRecyclerView.addItemDecoration(new MyDecoration(10));
                gradeRecyclerView.addItemDecoration(new MyDecoration(10));
                partRecyclerView.addItemDecoration(new MyDecoration(10));
                versionRecyclerView.addItemDecoration(new MyDecoration(10));
                subjectFilterItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        subjectFilterItemAdapter.onClick(position);
                        subjectDetailInfo = subjectFilterItemAdapter.getItem(position);

                    }
                });
                gradeFilterItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        gradeFilterItemAdapter.onClick(position);
                        gradeDetailInfo = gradeFilterItemAdapter.getItem(position);
                    }
                });
                patrFilterItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        patrFilterItemAdapter.onClick(position);
                        patrDetailInfo = patrFilterItemAdapter.getItem(position);
                    }
                });
                versionFilterItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        versionFilterItemAdapter.onClick(position);
                        versionDetailInfo = versionFilterItemAdapter.getItem(position);
                    }
                });

            }

            @Override
            public void onFail(String json) {

            }
        });

        RxView.clicks(tvReset).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
//                subjectFilterItemAdapter.onClick(0);
//                gradeFilterItemAdapter.onClick(0);
//                patrFilterItemAdapter.onClick(0);
                dismiss();
            }
        });

        RxView.clicks(tvConfirm).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(mContext, SearchActivity.class);
                intent.putExtra("page", 1);
                intent.putExtra("subject", subjectDetailInfo.getName());
                intent.putExtra("grade", gradeDetailInfo.getName());
                intent.putExtra("part", patrDetailInfo.getName());
                intent.putExtra("version", versionDetailInfo.getName());
                mContext.startActivity(intent);
                dismiss();
            }
        });


    }

    @Override
    public int getAnimationID() {
        return 0;
    }

    private List<VersionDetailInfo> createNewList(List<VersionDetailInfo> oldList) {

        if (oldList != null) {
            if (oldList.size() > 0 && !TextUtils.equals(mContext.getString(R.string.all), oldList.get(0).getName()))
                oldList.add(0, new VersionDetailInfo("", mContext.getString(R.string.all)));
        }
        return oldList;
    }

}

package com.yc.answer.index.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.RxSPTool;
import com.yc.answer.R;
import com.yc.answer.constant.SpConstant;

import java.util.List;

import butterknife.BindView;
import yc.com.base.BaseView;

/**
 * Created by wanglin  on 2018/4/18 13:40.
 */

public class SelectGradeView extends BaseView {
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.recyclerView_select)
    RecyclerView recyclerViewSelect;
    private List<String> mContents;
    private SelectGradeViewAdapter gradeViewAdapter;

    public SelectGradeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SelectGradeView);
        try {
            String name = ta.getString(R.styleable.SelectGradeView_select_name);
            if (!TextUtils.isEmpty(name)) {
                tvGrade.setText(name);
            }
            recyclerViewSelect.setLayoutManager(new GridLayoutManager(context, 3));
            gradeViewAdapter = new SelectGradeViewAdapter(mContents);
            recyclerViewSelect.setAdapter(gradeViewAdapter);
            recyclerViewSelect.addItemDecoration(new MyDecoration(RxImageTool.dip2px(5)));
            initListener();

        } finally {
            ta.recycle();
        }
    }

    private void initListener() {
        gradeViewAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RxSPTool.putString(mContext, SpConstant.SELECT_GRADE, gradeViewAdapter.getItem(position));
                if (listener != null) {
                    listener.onSelect(position);
                }
//                gradeViewAdapter.onClick(position);

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.select_grade_view;
    }


    public List<String> getmContents() {
        return mContents;
    }

    public void setContents(List<String> mContents) {
        this.mContents = mContents;
        gradeViewAdapter.setNewData(mContents);
    }

    public void clearSelect() {
        for (int i = 0; i < recyclerViewSelect.getChildCount(); i++) {
            recyclerViewSelect.getChildAt(i).setSelected(false);
        }
    }

    public void click(int positon) {
        recyclerViewSelect.getChildAt(positon).setSelected(true);
    }


    private OnSelectGradeListener listener;

    public void setOnSelectGradeListener(OnSelectGradeListener listener) {
        this.listener = listener;
    }

    public interface OnSelectGradeListener {
        void onSelect(int position);
    }


}

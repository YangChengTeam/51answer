package com.yc.answer.index.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.RxSPTool;
import com.yc.answer.R;
import com.yc.answer.constant.SpConstant;
import com.yc.answer.index.model.bean.VersionDetailInfo;
import com.yc.answer.index.ui.adapter.BookInfoItemAdapter;

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


    public SelectGradeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SelectGradeView);
        try {
            String name = ta.getString(R.styleable.SelectGradeView_select_name);
            int textColor = ta.getColor(R.styleable.SelectGradeView_select_name_color, ContextCompat.getColor(context, R.color.black_430206));
            float topMargin = ta.getDimension(R.styleable.SelectGradeView_select_margin_top, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics()));
            if (!TextUtils.isEmpty(name)) {
                tvGrade.setText(name);
            }
            tvGrade.setTextColor(textColor);
            LinearLayout.MarginLayoutParams params = (MarginLayoutParams) recyclerViewSelect.getLayoutParams();
            params.topMargin = (int) topMargin;
            recyclerViewSelect.setLayoutParams(params);

            recyclerViewSelect.setLayoutManager(new GridLayoutManager(mContext, 3));
        } finally {
            ta.recycle();
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.select_grade_view;
    }


    public void setContents(List<String> mContents) {
        final SelectGradeViewAdapter gradeViewAdapter = new SelectGradeViewAdapter(mContents);
        recyclerViewSelect.setAdapter(gradeViewAdapter);
        recyclerViewSelect.addItemDecoration(new MyDecoration(RxImageTool.dip2px(5)));
        gradeViewAdapter.setNewData(mContents);
        gradeViewAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RxSPTool.putString(mContext, SpConstant.SELECT_GRADE, gradeViewAdapter.getItem(position));
                if (listener != null) {
                    listener.onSelect(position, gradeViewAdapter.getItem(position));
                }
//                gradeViewAdapter.onClick(position);

            }
        });
    }

    public void setGrades(List<VersionDetailInfo> mContents, final int tag) {
        recyclerViewSelect.setLayoutManager(new GridLayoutManager(mContext, 3));
        final BookInfoItemAdapter bookInfoItemAdapter = new BookInfoItemAdapter(mContents);
        recyclerViewSelect.setAdapter(bookInfoItemAdapter);
        recyclerViewSelect.addItemDecoration(new MyDecoration(10, 10));
        bookInfoItemAdapter.setNewData(mContents);
        bookInfoItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (listener != null) {
                    setTag(tag);
                    listener.onSelect(position, bookInfoItemAdapter.getItem(position).getName());
                }
            }
        });

    }

    public void showTv(boolean isShow) {
        tvGrade.setVisibility(isShow ? VISIBLE : GONE);
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
        void onSelect(int position, String data);
    }


}

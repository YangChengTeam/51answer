package com.yc.ac.index.ui.fragment;

import android.text.TextUtils;

import com.vondear.rxtools.RxSPTool;
import com.yc.ac.R;
import com.yc.ac.constant.SpConstant;
import com.yc.ac.index.model.bean.VersionDetailInfo;
import com.yc.ac.index.ui.activity.PerfectBookDetailInfoActivity;
import com.yc.ac.index.ui.widget.SelectGradeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import yc.com.base.BaseFragment;

/**
 * Created by wanglin  on 2018/4/20 17:44.
 */

public class BookGradeFragment extends BaseFragment {

    @BindView(R.id.selectSmallView)
    SelectGradeView selectSmallView;
    @BindView(R.id.selectMiddleView)
    SelectGradeView selectMiddleView;
    @BindView(R.id.selectHighView)
    SelectGradeView selectHighView;

    private List<VersionDetailInfo> smallGradeList;
    private List<VersionDetailInfo> middleGradeList;
    private List<VersionDetailInfo> highGradeList;
    public static final int small = 0;
    public static final int middle = 1;
    public static final int high = 2;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_book_grade;
    }

    @Override
    public void init() {
        smallGradeList = new ArrayList<>();
        middleGradeList = new ArrayList<>();
        highGradeList = new ArrayList<>();

        final String[] smallGrades = getActivity().getResources().getStringArray(R.array.small_grade);
        final String[] middleGrades = getActivity().getResources().getStringArray(R.array.middle_grade);
        final String[] highGrades = getActivity().getResources().getStringArray(R.array.senior_grade);
        for (String smallGrade : smallGrades) {
            smallGradeList.add(new VersionDetailInfo("", smallGrade));
        }
        for (String middleGrade : middleGrades) {
            middleGradeList.add(new VersionDetailInfo("", middleGrade));
        }
        for (String highGrade : highGrades) {
            highGradeList.add(new VersionDetailInfo("", highGrade));
        }
        selectSmallView.setGrades(smallGradeList, small);
        selectMiddleView.setGrades(middleGradeList, middle);
        selectHighView.setGrades(highGradeList, high);
        String saveGrade = RxSPTool.getString(getActivity(), SpConstant.BOOK_GRADE);
        if (!TextUtils.isEmpty(saveGrade)) {
            String[] splits = saveGrade.split("-");
            final int currentPos = Integer.parseInt(splits[0]);//当前的位置
            int currentView = Integer.parseInt(splits[1]);//最后是哪个recyclerView
            setGradeData(smallGrades, middleGrades, highGrades, currentPos, currentView);
        } else {
            setData(selectSmallView, 0, smallGrades);
        }

        setSelect(selectSmallView);
        setSelect(selectMiddleView);
        setSelect(selectHighView);


    }

    private void setGradeData(final String[] smallGrades, final String[] middleGrades, final String[] highGrades, final int currentPos, int currentView) {
        if (small == currentView) {
            setData(selectSmallView, currentPos, smallGrades);
        } else if (middle == currentView) {
            setData(selectMiddleView, currentPos, middleGrades);
        } else if (high == currentView) {
            setData(selectHighView, currentPos, highGrades);
        }
    }

    private void setData(final SelectGradeView view, final int pos, final String[] datas) {
        view.post(new Runnable() {
            @Override
            public void run() {
                view.click(pos);
                ((PerfectBookDetailInfoActivity) getActivity()).transmitData(pos, datas[pos]);
            }
        });
    }

    private void setSelect(final SelectGradeView view) {
        view.setOnSelectGradeListener(new SelectGradeView.OnSelectGradeListener() {
            @Override
            public void onSelect(int position, String data) {
                selectMiddleView.clearSelect();
                selectHighView.clearSelect();
                selectSmallView.clearSelect();
                view.click(position);
                String currentPosInfo = position + "-" + view.getTag() + "-" + data;
                ((PerfectBookDetailInfoActivity) getActivity()).transmitData(position, data);
                RxSPTool.putString(getActivity(), SpConstant.BOOK_GRADE, currentPosInfo);
            }
        });
    }


}

package com.yc.ac.index.ui.fragment;

import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.vondear.rxtools.RxSPTool;
import com.yc.ac.R;
import com.yc.ac.constant.SpConstant;
import com.yc.ac.index.model.bean.VersionDetailInfo;
import com.yc.ac.index.model.bean.VersionInfo;
import com.yc.ac.index.ui.activity.PerfectBookDetailInfoActivity;
import com.yc.ac.index.ui.widget.SelectGradeView;

import java.util.List;

import butterknife.BindView;
import yc.com.base.BaseFragment;
import yc.com.base.CommonInfoHelper;

/**
 * Created by wanglin  on 2018/4/20 17:44.
 */

public class BookSubjectFragment extends BaseFragment {


    @BindView(R.id.selectGradeView)
    SelectGradeView selectGradeView;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_book_subject;
    }

    @Override
    public void init() {

        CommonInfoHelper.getO(getActivity(), SpConstant.INDEX_VERSION, new TypeReference<VersionInfo>() {
        }.getType(), new CommonInfoHelper.onParseListener<VersionInfo>() {
            @Override
            public void onParse(VersionInfo versionInfo) {
                final List<VersionDetailInfo> versionDetailInfos = createNewData(versionInfo.getSubject());
                selectGradeView.setGrades(versionDetailInfos, 0);
                selectGradeView.showTv(false);
                selectGradeView.post(new Runnable() {
                    @Override
                    public void run() {
                        String subject = RxSPTool.getString(getActivity(), SpConstant.BOOK_SUBJECT);
                        int subjectPos = 0;
                        if (!TextUtils.isEmpty(subject)) {
                            String[] split = subject.split("-");
                            subjectPos = Integer.parseInt(split[1]);
                        }
                        selectGradeView.click(subjectPos);
                        ((PerfectBookDetailInfoActivity) getActivity()).transmitData(subjectPos, versionDetailInfos.get(subjectPos).getName());
                    }
                });

                selectGradeView.setOnSelectGradeListener(new SelectGradeView.OnSelectGradeListener() {
                    @Override
                    public void onSelect(int position, String data) {
                        selectGradeView.clearSelect();
                        selectGradeView.click(position);
                        ((PerfectBookDetailInfoActivity) getActivity()).transmitData(position, data);
                    }
                });

            }

            @Override
            public void onFail(String json) {

            }
        });


    }

    private List<VersionDetailInfo> createNewData(List<VersionDetailInfo> list) {
        if (list != null && list.size() > 0) {
            list.remove(0);
            list.add(new VersionDetailInfo("", "其他"));

        }
        return list;
    }

}

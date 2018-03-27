package com.yc.answer.utils;

import com.alibaba.fastjson.JSON;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.RxTool;
import com.yc.answer.constant.SpConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglin  on 2018/3/14 16:18.
 */

public class SearchHistoryHelper {

    public static List<String> mHistoryList;

    public static List<String> getHistoryList() {
        if (mHistoryList != null) {
            return mHistoryList;
        }
        try {
            mHistoryList = JSON.parseArray(RxSPTool.getString(RxTool.getContext(), SpConstant.HISTORY), String.class);
        } catch (Exception e) {
            RxLogTool.e("-->" + e.getMessage());
        }
        return mHistoryList;
    }

    public static void setHistoryList(List<String> historyList) {
        mHistoryList = historyList;
        try {
            RxSPTool.putString(RxTool.getContext(), SpConstant.HISTORY, JSON.toJSONString(historyList));
        } catch (Exception e) {
            RxLogTool.e("-->" + e.getMessage());
        }
    }


    public static void saveHistoryList(final String text) {


        if (mHistoryList == null) {
            mHistoryList = new ArrayList<>();
        }
        if (mHistoryList.contains(text)) {
            mHistoryList.remove(mHistoryList.indexOf(text));
        }
        mHistoryList.add(0, text);
        SearchHistoryHelper.setHistoryList(mHistoryList);

    }
}

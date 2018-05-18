package com.yc.answer.utils;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.answer.R;
import com.yc.answer.index.model.bean.BookInfo;

/**
 * Created by wanglin  on 2018/4/19 11:11.
 */

public class SubjectHelper {

    public static void setSubject(BaseViewHolder helper, BookInfo item,int imgId) {
        switch (item.getSubject()) {
            case "语文":
                helper.setImageResource(imgId, R.mipmap.yuwen);
                break;
            case "数学":
                helper.setImageResource(imgId, R.mipmap.shuxue);
                break;
            case "英语":
                helper.setImageResource(imgId, R.mipmap.english);
                break;
            case "物理":
                helper.setImageResource(imgId, R.mipmap.wuli);
                break;
            case "生物":
                helper.setImageResource(imgId, R.mipmap.shengwu);
                break;
            case "化学":
                helper.setImageResource(imgId, R.mipmap.huawue);
                break;
            case "政治":
                helper.setImageResource(imgId, R.mipmap.zhengzhi);
                break;
            case "地理":
                helper.setImageResource(imgId, R.mipmap.dili);
                break;
            case "历史":
                helper.setImageResource(imgId, R.mipmap.history);
                break;
        }
    }
}

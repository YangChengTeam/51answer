package com.yc.answer.index.model.bean;

import java.util.List;

/**
 * Created by wanglin  on 2018/3/8 16:18.
 */

public class FilterInfo {
    private String title;
    private List<VersionDetailInfo> list;

    public FilterInfo() {
    }

    public FilterInfo(String title, List<VersionDetailInfo> list) {
        this.title = title;
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<VersionDetailInfo> getList() {
        return list;
    }

    public void setList(List<VersionDetailInfo> list) {
        this.list = list;
    }
}

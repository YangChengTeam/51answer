package com.yc.ac.index.model.bean;

/**
 * Created by wanglin  on 2018/3/7 17:18.
 */

public class VersionDetailInfo {
    private String id;
    private String name;


    public VersionDetailInfo() {
    }

    public VersionDetailInfo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

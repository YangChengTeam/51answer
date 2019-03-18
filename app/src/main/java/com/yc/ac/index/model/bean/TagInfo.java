package com.yc.ac.index.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by wanglin  on 2019/3/14 17:32.
 */
public class TagInfo {
    @JSONField(name = "tagid")
    private String id;
    @JSONField(name = "tagname")
    private String title;

    //专题字段
    private String ztid;
    private String ztname;
    private String ztimg;
    private String ztpath;

    public TagInfo() {
    }

    public TagInfo(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getZtid() {
        return ztid;
    }

    public void setZtid(String ztid) {
        this.ztid = ztid;
    }

    public String getZtname() {
        return ztname;
    }

    public void setZtname(String ztname) {
        this.ztname = ztname;
    }

    public String getZtimg() {
        return ztimg;
    }

    public void setZtimg(String ztimg) {
        this.ztimg = ztimg;
    }

    public String getZtpath() {
        return ztpath;
    }

    public void setZtpath(String ztpath) {
        this.ztpath = ztpath;
    }
}

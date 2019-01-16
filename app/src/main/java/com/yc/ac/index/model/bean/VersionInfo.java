package com.yc.ac.index.model.bean;

import java.util.List;

/**
 * Created by wanglin  on 2018/3/7 17:57.
 */

public class VersionInfo {
    private List<VersionDetailInfo> version; // 版本选项
    private List<VersionDetailInfo> grade;// 年级
    private List<VersionDetailInfo> part_type;// 上下册
    private List<VersionDetailInfo> subject;// 学科
    private List<VersionDetailInfo> flag; // 标记

    public List<VersionDetailInfo> getVersion() {
        return version;
    }

    public void setVersion(List<VersionDetailInfo> version) {
        this.version = version;
    }

    public List<VersionDetailInfo> getGrade() {
        return grade;
    }

    public void setGrade(List<VersionDetailInfo> grade) {
        this.grade = grade;
    }

    public List<VersionDetailInfo> getPart_type() {
        return part_type;
    }

    public void setPart_type(List<VersionDetailInfo> part_type) {
        this.part_type = part_type;
    }

    public List<VersionDetailInfo> getSubject() {
        return subject;
    }

    public void setSubject(List<VersionDetailInfo> subject) {
        this.subject = subject;
    }

    public List<VersionDetailInfo> getFlag() {
        return flag;
    }

    public void setFlag(List<VersionDetailInfo> flag) {
        this.flag = flag;
    }
}

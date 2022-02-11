package com.yc.ac.index.model.bean;

/**
 * Created by wanglin  on 2018/   3/7 17:02.
 * 幻灯信息
 */

public class SlideInfo {
    private String id;// Slide ID
    private String title;// 标题
    private String img;// 图片
    private String group;// 分组
    private String type;// 行为类型
    private String link;// 行为链接
    private String sort;// 排序
    private String add_time;// 添加时间
    private String add_date;// 添加日期

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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }
}

package com.yc.ac.index.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class PayItemInfo {
    private int id;
    @JSONField(name = "name")
    private String title;
    private double price;

    @JSONField(name = "describe")
    private String desc;
    @JSONField(name = "old_price")
    private double origin_price;

    public PayItemInfo() {
    }

    public PayItemInfo(int id, String title, double price, String desc, double origin_price, int isHot) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.desc = desc;
        this.origin_price = origin_price;
        this.isHot = isHot;
    }

    private int isHot;//是否爆品

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getOrigin_price() {
        return origin_price;
    }

    public void setOrigin_price(double origin_price) {
        this.origin_price = origin_price;
    }

    public int getIsHot() {
        return isHot;
    }

    public void setIsHot(int isHot) {
        this.isHot = isHot;
    }
}

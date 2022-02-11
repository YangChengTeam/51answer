package com.yc.ac.setting.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class OrderInfo {

    private int id;
    @JSONField(name = "order_num")
    private String orderno;//订单号
    @JSONField(name = "name")
    private String title;//订单名称
    private double money;//订单金额
    @JSONField(name = "time")
    private String date;//订单时间
    private int status;//支付状态 1未支付 2支付成功


    public OrderInfo() {
    }

    public OrderInfo(int id, String orderno, String title, double money, String date) {
        this.id = id;
        this.orderno = orderno;
        this.title = title;
        this.money = money;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

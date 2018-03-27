package com.yc.answer.setting.model.bean;

/**
 * Created by wanglin  on 2018/3/7 15;01.
 */

public class UserInfo {

    private String id;// 用户ID
    private String name;// 用户名
    private String nick_name;// 昵称
    private String mobile; //手机号
    private String face; //头像
    private int vip; //vip等级，0; 不是vip，1; vip
    private String vip_end_time;//vip到期时间，时间戳（秒）

    private boolean isLogin;

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

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public String getVip_end_time() {
        return vip_end_time;
    }

    public void setVip_end_time(String vip_end_time) {
        this.vip_end_time = vip_end_time;
    }

    public boolean isLogin() {


        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}

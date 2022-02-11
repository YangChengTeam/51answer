package com.yc.ac.setting.model.bean;

public class NotificationInfo {
    private String title;
    private int id;
    private long date;

    public NotificationInfo() {
    }

    public NotificationInfo(String title, int id, long date) {
        this.title = title;
        this.id = id;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}

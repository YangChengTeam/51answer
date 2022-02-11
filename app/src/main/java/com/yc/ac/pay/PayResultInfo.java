package com.yc.ac.pay;

/**
 * Created by wanglin on 2021/5/20 10:30
 */
public class PayResultInfo {
    private int code;
    private String message;

    public PayResultInfo(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

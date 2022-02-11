package com.yc.ac.pay;

public interface PayResultListener {

    void paySuccess(String result);

    void payFailure(String errMsg);

    void payCancel();
}

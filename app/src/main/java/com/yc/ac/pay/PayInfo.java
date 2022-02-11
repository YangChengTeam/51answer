package com.yc.ac.pay;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by wanglin on 2021/5/19 19:41
 */
public class PayInfo {
    @JSONField(name = "order_info")
    private String orderInfo;

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }
}

package com.yc.ac.setting.model.engine;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by wanglin on 2021/5/20 9:44
 */
public class WxPayInfo {

    @JSONField(name = "order_info")
    private OrderParamsInfo orderInfo;

    public OrderParamsInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderParamsInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}

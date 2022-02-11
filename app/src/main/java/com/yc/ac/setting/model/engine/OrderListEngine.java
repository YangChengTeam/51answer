package com.yc.ac.setting.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.vondear.rxtools.view.progressing.style.FadingCircle;
import com.yc.ac.base.BaseEngine;
import com.yc.ac.constant.NetConstant;
import com.yc.ac.setting.model.bean.OrderInfo;

import java.util.List;

import rx.Observable;


/**
 * Created by wanglin on 2021/5/20 8:55
 */
public class OrderListEngine extends BaseEngine {
    public OrderListEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<List<OrderInfo>>> getOrderInfoList() {
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.order_list_url, new TypeReference<ResultInfo<List<OrderInfo>>>() {
        }.getType(), null, getHeaders(), false, false, false);
    }
}

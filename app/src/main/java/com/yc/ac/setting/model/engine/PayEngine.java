package com.yc.ac.setting.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.ac.base.BaseEngine;
import com.yc.ac.constant.NetConstant;
import com.yc.ac.index.model.bean.PayItemInfo;
import com.yc.ac.pay.PayInfo;
import com.yc.ac.setting.model.bean.TokenInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin on 2021/5/19 17:13
 */
public class PayEngine extends BaseEngine {
    public PayEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<List<PayItemInfo>>> getPayItemInfoList() {
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.pay_item_list_url, new TypeReference<ResultInfo<List<PayItemInfo>>>() {
        }.getType(), null, false, false, false);
    }


    public Observable<ResultInfo<PayInfo>> aliPay(String userId, String goodsId, String classId, String articleId){
        Map<String,String> params= new HashMap<>();
        params.put("user_id",userId);
        params.put("goods_id",goodsId);
        params.put("class_id",classId);
        params.put("article_id",articleId);
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.alipay_url, new TypeReference<ResultInfo<PayInfo>>() {
        }.getType(), params, getHeaders(),false, false, false);

    }

    public Observable<ResultInfo<WxPayInfo>> wxPay(String userId, String goodsId, String classId, String articleId){
        Map<String,String> params= new HashMap<>();
        params.put("user_id",userId);
        params.put("goods_id",goodsId);
        params.put("class_id",classId);
        params.put("article_id",articleId);
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.wxpay_url, new TypeReference<ResultInfo<WxPayInfo>>() {
        }.getType(), params, getHeaders(),false, false, false);
    }
}

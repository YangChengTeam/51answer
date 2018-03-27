package com.yc.answer.setting.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.answer.base.BaseEngine;
import com.yc.answer.constant.NetConstant;
import com.yc.answer.setting.model.bean.UserInfo;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/3/15 10:41.
 */

public class BindPhoneEngine extends BaseEngine {
    public BindPhoneEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<UserInfo>> changePhone(String mobile, String code) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("code", code);

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.user_rebind_url, new TypeReference<ResultInfo<UserInfo>>() {
        }.getType(), params, getHeaders(), false, false, false);

    }


    public Observable<ResultInfo<UserInfo>> bindPhone(String mobile, String code) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("code", code);

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.user_bind_url, new TypeReference<ResultInfo<UserInfo>>() {
        }.getType(), params, getHeaders(), false, false, false);

    }

}

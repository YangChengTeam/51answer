package com.yc.answer.setting.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.answer.base.BaseEngine;
import com.yc.answer.constant.NetConstant;
import com.yc.answer.setting.model.bean.ShareInfo;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/3/22 08:59.
 */

public class ShareEngine extends BaseEngine {
    public ShareEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<String>> share(String book_id) {

        Map<String, String> params = new HashMap<>();

        params.put("book_id", book_id);

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.book_share_url, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, getHeaders(), false, false, false);

    }
}

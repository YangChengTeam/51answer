package com.yc.answer.collect.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.answer.base.BaseEngine;
import com.yc.answer.constant.NetConstant;
import com.yc.answer.index.model.bean.BookInfoWrapper;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/3/8 18:45.
 */

public class CollectEngine extends BaseEngine {
    public CollectEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<BookInfoWrapper>> getCollectList(int page, int limit) {

        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("limit", limit + "");

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.book_myfavorite_url, new TypeReference<ResultInfo<BookInfoWrapper>>() {
        }.getType(), params, getHeaders(), false, false, false);


    }
}

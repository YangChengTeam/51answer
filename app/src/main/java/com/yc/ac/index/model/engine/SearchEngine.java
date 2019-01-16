package com.yc.ac.index.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.ac.base.BaseEngine;
import com.yc.ac.constant.NetConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/3/20 13:30.
 */

public class SearchEngine extends BaseEngine {
    public SearchEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<List<String>>> getTintList(String word) {
        Map<String, String> params = new HashMap<>();
        params.put("word", word);


        return HttpCoreEngin.get(mContext).rxpost(NetConstant.book_tip_url, new TypeReference<ResultInfo<List<String>>>() {
        }.getType(), params, getHeaders(), false, false, false);

    }
}

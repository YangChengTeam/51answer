package com.yc.answer.index.model.engine;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.vondear.rxtools.activity.ActivityScanerCode;
import com.yc.answer.base.BaseEngine;
import com.yc.answer.constant.NetConstant;
import com.yc.answer.constant.SpConstant;
import com.yc.answer.index.model.bean.BookInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import yc.com.base.CacheUtils;
import yc.com.base.CommonInfoHelper;

/**
 * Created by wanglin  on 2018/3/12 14:43.
 */

public class AnswerDetailEngine extends BaseEngine {
    public AnswerDetailEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<String>> favoriteAnswer(String book_id) {
        Map<String, String> params = new HashMap<>();
        params.put("book_id", book_id);

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.book_favorite_url, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, getHeaders(), false, false, false);

    }

}

package com.yc.ac.index.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.ac.base.BaseEngine;
import com.yc.ac.constant.NetConstant;
import com.yc.ac.index.model.bean.BookInfoWrapper;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/3/8 09:14.
 */

public class IndexBookEngine extends BaseEngine {
    public IndexBookEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<BookInfoWrapper>> getBookList(int page, int limit, String subject_id) {
        Map<String, String> params = new HashMap<>();

        params.put("page", page + "");
        params.put("limit", limit + "");
        params.put("subject_id", subject_id);

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.book_index_url, new TypeReference<ResultInfo<BookInfoWrapper>>() {
        }.getType(), params, getHeaders(), false, false, false);


    }
}

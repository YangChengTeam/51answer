package com.yc.ac.index.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.ac.base.BaseEngine;
import com.yc.ac.constant.NetConstant;
import com.yc.ac.index.model.bean.BookInfo;
import com.yc.ac.index.model.bean.BookInfoWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/4/24 16:06.
 */

public class UploadBookListEngine extends BaseEngine {
    public UploadBookListEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<BookInfoWrapper>> getUploadBookList(int page, int limit) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("limit", limit + "");
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.upload_list_url, new TypeReference<ResultInfo<BookInfoWrapper>>() {
        }.getType(), params, getHeaders(), false, false, false);
    }
}

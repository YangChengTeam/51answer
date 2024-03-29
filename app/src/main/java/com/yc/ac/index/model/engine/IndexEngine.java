package com.yc.ac.index.model.engine;


import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.ac.base.BaseEngine;
import com.yc.ac.constant.NetConstant;
import com.yc.ac.index.model.bean.BookInfoWrapper;
import com.yc.ac.index.model.bean.SlideInfo;
import com.yc.ac.index.model.bean.TagInfo;
import com.yc.ac.index.model.bean.TagInfoWrapper;
import com.yc.ac.index.model.bean.VersionInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wanglin  on 2018/2/27 13:34.
 */

public class IndexEngine extends BaseEngine {

    public IndexEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<List<SlideInfo>>> getSlideInfos(String group) {

        Map<String, String> params = new HashMap<>();

        params.put("group", group);

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.slide_index_url, new TypeReference<ResultInfo<List<SlideInfo>>>() {
        }.getType(), params, getHeaders(), false, false, false);

    }


    public Observable<ResultInfo<VersionInfo>> getVersionList() {

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.book_version_url, new TypeReference<ResultInfo<VersionInfo>>() {
        }.getType(), null, getHeaders(), false, false, false);
    }


    public Observable<ResultInfo<TagInfoWrapper>> getTagInfos() {
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.index_tag_url, new TypeReference<ResultInfo<TagInfoWrapper>>() {
        }.getType(), null, getHeaders(), false, false, false);

    }

    public Observable<ResultInfo<TagInfoWrapper>> getZtInfos() {
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.index_zt_url, new TypeReference<ResultInfo<TagInfoWrapper>>() {
        }.getType(), null, getHeaders(), false, false, false);
    }

}

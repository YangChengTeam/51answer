package com.yc.answer.index.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.answer.base.BaseEngine;
import com.yc.answer.constant.NetConstant;
import com.yc.answer.index.model.bean.OssInfo;

import rx.Observable;


/**
 * Created by wanglin  on 2018/4/23 16:03.
 */

public class UploadBookEngine extends BaseEngine {
    public UploadBookEngine(Context context) {
        super(context);
    }

    public Observable<OssInfo> getOssInfo() {
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.oss_api_url, new TypeReference<OssInfo>() {
        }.getType(), null, getHeaders(), false, false, false);
    }
}

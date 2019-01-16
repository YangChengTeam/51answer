package com.yc.ac.index.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.ac.base.BaseEngine;
import com.yc.ac.constant.NetConstant;
import com.yc.ac.index.model.bean.OssInfo;

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

package com.yc.ac.setting.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.securityhttp.net.entry.UpFileInfo;
import com.yc.ac.base.BaseEngine;
import com.yc.ac.constant.NetConstant;
import com.yc.ac.index.model.bean.PayItemInfo;
import com.yc.ac.setting.model.bean.UploadInfo;
import com.yc.ac.utils.UserInfoHelper;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/3/13 14:35.
 */

public class SettingEngine extends BaseEngine {
    public SettingEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<String>> userCancellation() {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", UserInfoHelper.getUId());
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.cancellation_url, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, getHeaders(), false, false, false);
    }

}

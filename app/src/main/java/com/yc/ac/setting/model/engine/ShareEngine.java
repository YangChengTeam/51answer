package com.yc.ac.setting.model.engine;

import android.content.Context;
import android.text.TextUtils;
import android.view.TextureView;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.utils.ToastUtil;
import com.yc.ac.base.BaseEngine;
import com.yc.ac.constant.NetConstant;
import com.yc.ac.setting.model.bean.ShareInfo;
import com.yc.ac.utils.UserInfoHelper;

import org.w3c.dom.Text;

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


    public Observable<ResultInfo<String>> shareMoney() {
        Map<String, String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(UserInfoHelper.getToken()))
            headers.put("Authorization", "Bearer " + UserInfoHelper.getToken());
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.task_share_reward_url, new TypeReference<ResultInfo<String>>() {
        }.getType(), null, headers, false, false, false);

    }
}

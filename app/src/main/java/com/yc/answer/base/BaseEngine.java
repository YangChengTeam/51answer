package com.yc.answer.base;

import android.content.Context;
import android.text.TextUtils;

import com.yc.answer.utils.UserInfoHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanglin  on 2018/2/27 13:32.
 */

public class BaseEngine {
    protected Context mContext;


    public BaseEngine(Context context) {
        this.mContext = context;

    }

    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(UserInfoHelper.getToken()))
            headers.put("Authorization", "Bearer " + UserInfoHelper.getToken());
        return headers;
    }
}

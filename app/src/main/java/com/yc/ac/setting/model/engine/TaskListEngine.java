package com.yc.ac.setting.model.engine;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.ac.base.BaseEngine;
import com.yc.ac.constant.NetConstant;
import com.yc.ac.setting.model.bean.TaskLisInfoWrapper;
import com.yc.ac.utils.UserInfoHelper;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/9/13 17:43.
 */
public class TaskListEngine extends BaseEngine {
    public TaskListEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<TaskLisInfoWrapper>> getTaskInfoList() {

        Map<String, String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(UserInfoHelper.getToken()))
            headers.put("Authorization", "Bearer " + UserInfoHelper.getToken());

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.task_list_url, new TypeReference<ResultInfo<TaskLisInfoWrapper>>() {
        }.getType(), null,headers, false, false, false);

    }
}

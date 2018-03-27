package com.yc.answer.index.model.engine;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.answer.base.BaseEngine;
import com.yc.answer.constant.NetConstant;
import com.yc.answer.index.model.bean.VersionDetailInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;


/**
 * Created by wanglin  on 2018/3/12 08:49.
 */

public class BookConditionEngine extends BaseEngine {
    public BookConditionEngine(Context context) {
        super(context);
    }

}

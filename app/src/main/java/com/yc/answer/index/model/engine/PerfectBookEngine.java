package com.yc.answer.index.model.engine;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.answer.base.BaseEngine;
import com.yc.answer.constant.NetConstant;
import com.yc.answer.index.model.bean.BookInfo;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/4/24 15:20.
 */

public class PerfectBookEngine extends BaseEngine {
    public PerfectBookEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<String>> perfectBookInfo(BookInfo bookInfo) {
        Map<String, String> params = new HashMap<>();
        params.put("id", bookInfo.getBookId());
        params.put("cover_img", bookInfo.getCover_img());
        params.put("answer_list", JSON.toJSONString(bookInfo.getAnswer_list()));
        params.put("name", bookInfo.getName());
        params.put("subject", bookInfo.getSubject());
        params.put("grade", bookInfo.getGrade());

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.user_img_upload_book_url, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, getHeaders(), false, false, false);
    }
}

package com.yc.answer.utils;

import com.alibaba.fastjson.JSON;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.RxTool;
import com.yc.answer.constant.SpConstant;
import com.yc.answer.setting.model.bean.ShareInfo;

/**
 * Created by wanglin  on 2018/3/22 10:47.
 */

public class ShareInfoHelper {
    private static ShareInfo mShareInfo;

    public static ShareInfo getShareInfo() {
        if (mShareInfo != null) {
            return mShareInfo;
        }
        try {
            mShareInfo = JSON.parseObject(RxSPTool.getString(RxTool.getContext(), SpConstant.SHARE_INFO), ShareInfo.class);

        } catch (Exception e) {
            RxLogTool.e("-->" + e.getMessage());
        }

        return mShareInfo;
    }

    public static void setShareInfo(ShareInfo shareInfo) {
        mShareInfo = shareInfo;
        try {
            RxSPTool.putString(RxTool.getContext(), SpConstant.SHARE_INFO, JSON.toJSONString(shareInfo));
        } catch (Exception e) {
            RxLogTool.e("-->" + e.getMessage());
        }
    }
}

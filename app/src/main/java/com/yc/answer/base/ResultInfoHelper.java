package com.yc.answer.base;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.answer.constant.HttpStatus;
import com.yc.answer.utils.UserInfoHelper;

/**
 * Created by zhangkai on 2017/8/3.
 */

public class ResultInfoHelper {
    public static <T> void handleResultInfo(ResultInfo<T> resultInfo, Callback callback) {
        if (EmptyUtils.isEmpty(resultInfo)) {
            callback.resultInfoEmpty(HttpConfig.NET_ERROR);
            return;
        }

        if (resultInfo.code == HttpConfig.STATUS_OK) {
            callback.reulstInfoOk();
        } else if (resultInfo.code == HttpStatus.TOKEN_EXPIRED) {
            UserInfoHelper.setToken("");
            UserInfoHelper.setUserInfo(null);
        } else {
            callback.resultInfoNotOk(getMessage(resultInfo.message, HttpConfig.SERVICE_ERROR));
        }
    }

    public static String getMessage(String message, String desc) {
        return EmptyUtils.isEmpty(message) ? desc : message;
    }

    public interface Callback {
        void resultInfoEmpty(String message);

        void resultInfoNotOk(String message);

        void reulstInfoOk();
    }

}

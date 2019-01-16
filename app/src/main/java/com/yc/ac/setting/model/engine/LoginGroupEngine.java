package com.yc.ac.setting.model.engine;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.ac.base.BaseEngine;
import com.yc.ac.constant.NetConstant;
import com.yc.ac.setting.model.bean.TokenInfo;
import com.yc.ac.setting.model.bean.UserInfo;
import com.yc.ac.utils.UserInfoHelper;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Result;

import rx.Observable;

/**
 * Created by wanglin  on 2018/3/7 15:00.
 */

public class LoginGroupEngine extends BaseEngine {
    public LoginGroupEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<TokenInfo>> register(String mobile, String code, String password) {

        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("code", code);
        params.put("password", password);
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.user_reg_url, new TypeReference<ResultInfo<TokenInfo>>() {
        }.getType(), params, false, false, false);

    }

    /**
     * 登录
     *
     * @param mobile
     * @param password
     * @return
     */
    public Observable<ResultInfo<TokenInfo>> login(String mobile, String password, String code) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        if (!TextUtils.isEmpty(password))
            params.put("password", password);
        if (!TextUtils.isEmpty(code))
            params.put("code", code);
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.user_login_url, new TypeReference<ResultInfo<TokenInfo>>() {
        }.getType(), params, false, false, false);

    }

    /**
     * 找回密码
     * mobile: 手机号
     * code: 短信码
     * new_password: 密码
     *
     * @return
     */
    public Observable<ResultInfo<String>> resetPassword(String mobile, String code, String new_password) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("code", code);
        params.put("new_password", new_password);

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.user_reset_url, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, getHeaders(), false, false, false);

    }

    /**
     * token: 第三方登录凭证
     * sns: 1: QQ；2: 微信openID；3: 微信unionId；4: 微博
     * [nick_name]: 昵称
     * [face]: 头像
     * [user_id]: 用户ID，默认为零，否则绑定至用户
     */
    public Observable<ResultInfo<TokenInfo>> snsLogin(String token, String sns, String nick_name, String face) {
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("sns", sns);
        params.put("nick_name", nick_name);
        params.put("face", face);
        params.put("user_id", UserInfoHelper.isLogin() ? UserInfoHelper.getUserInfo().getId() : "0");
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.user_sns_url, new TypeReference<ResultInfo<TokenInfo>>() {
        }.getType(), params, true, true, true);

    }


}

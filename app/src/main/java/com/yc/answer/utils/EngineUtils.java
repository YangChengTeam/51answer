package com.yc.answer.utils;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.text.TextUtils;
import android.view.TextureView;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.securityhttp.net.entry.UpFileInfo;
import com.yc.answer.constant.NetConstant;
import com.yc.answer.index.model.bean.BookInfo;
import com.yc.answer.index.model.bean.BookInfoWrapper;
import com.yc.answer.setting.model.bean.QbInfoWrapper;
import com.yc.answer.setting.model.bean.ShareInfo;
import com.yc.answer.setting.model.bean.TaskLisInfoWrapper;
import com.yc.answer.setting.model.bean.UploadInfo;
import com.yc.answer.setting.model.bean.UserInfo;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.internal.util.unsafe.MpmcArrayQueue;

/**
 * Created by wanglin  on 2018/3/7 15:13.
 */

public class EngineUtils {

    /**
     * 获取用户资料
     */
    public static Observable<ResultInfo<UserInfo>> getUserInfo(Context context, String token) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);

        return HttpCoreEngin.get(context).rxpost(NetConstant.user_info_url, new TypeReference<ResultInfo<UserInfo>>() {
        }.getType(), null, headers, false, false, false);
    }

    /**
     * 获取验证码
     */
    public static Observable<ResultInfo<String>> getCode(Context context, String phone) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        Map<String, String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(UserInfoHelper.getToken()))
            headers.put("Authorization", "Bearer " + UserInfoHelper.getToken());

        return HttpCoreEngin.get(context).rxpost(NetConstant.user_code_url, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, headers, false, false, false);

    }

    /**
     * 获取书本列表
     * page: 页码
     * limit: 数量
     * [name]: 书名
     * [code]: 唯一码
     * <p>
     * [grade_id]: 年级。取值: 1~12
     * [grade]: 年级。任选一个做条件
     * <p>
     * [part_type_id]: 上下册。取值: 0: 上册，1: 下册，2:  全册
     * [part_type]: 上下册。任选一个做条件
     * <p>
     * [version_id]: 书本版本ID。
     * [version]: 书本版本ID。任选一个做条件
     * <p>
     * [subject_id]: 学科ID
     * [subject]: 学科。任选一个做条件
     * <p>
     * [flag_id]: 标记。取值: 1: 推荐；2: 热门
     * [flag]: 标记。任选一个做条件
     * <p>
     * [year]: 年份
     * [user_id]: 用户ID
     **/
    public static Observable<ResultInfo<BookInfoWrapper>> getBookInfoList(Context context, int page, int limit, String name, String code, String grade_id, String grade,
                                                                          String part_type_id, String part_type, String version_id, String version, String subject_id,
                                                                          String subject, String flag_id, String year, String latitude, String longitude) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("limit", limit + "");
        if (!TextUtils.isEmpty(name)) params.put("name", name);
        if (!TextUtils.isEmpty(code)) params.put("code", code);
        if (!TextUtils.isEmpty(grade_id)) params.put("grade_id", grade_id);
        if (!TextUtils.isEmpty(grade)) params.put("grade", grade);
        if (!TextUtils.isEmpty(part_type_id)) params.put("part_type_id", part_type_id);
        if (!TextUtils.isEmpty(part_type)) params.put("part_type", part_type);
        if (!TextUtils.isEmpty(version_id)) params.put("version_id", version_id);
        if (!TextUtils.isEmpty(version)) params.put("version", version);
        if (!TextUtils.isEmpty(subject_id)) params.put("subject_id", subject_id);
        if (!TextUtils.isEmpty(subject)) params.put("subject", subject);
        if (!TextUtils.isEmpty(flag_id)) params.put("flag_id", flag_id);
        if (!TextUtils.isEmpty(year)) params.put("year", year);
        if (!TextUtils.isEmpty(latitude)) params.put("latitude", latitude);
        if (!TextUtils.isEmpty(longitude)) params.put("longitude", longitude);
        Map<String, String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(UserInfoHelper.getToken()))
            headers.put("Authorization", "Bearer " + UserInfoHelper.getToken());

        return HttpCoreEngin.get(context).rxpost(NetConstant.book_index_url, new TypeReference<ResultInfo<BookInfoWrapper>>() {
        }.getType(), params, headers, false, false, false);


    }


    /**
     * 获取答案详情
     *
     * @param context
     * @param book_id
     * @return
     */
    public static Observable<ResultInfo<BookInfo>> getBookDetailInfo(Context context, String book_id) {
        Map<String, String> params = new HashMap<>();
        params.put("book_id", book_id);
        Map<String, String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(UserInfoHelper.getToken()))
            headers.put("Authorization", "Bearer " + UserInfoHelper.getToken());
        return HttpCoreEngin.get(context).rxpost(NetConstant.book_answer_url, new TypeReference<ResultInfo<BookInfo>>() {
        }.getType(), params, headers, false, false, false);
    }

    /**
     * [user_id]: 用户ID
     * [nick_name]: 昵称
     * [face]: 头像
     * [password]: 密码
     */

    public static Observable<ResultInfo<UserInfo>> updateInfo(Context context, String nick_name, String face, String password) {

        Map<String, String> params = new HashMap<>();

        if (!TextUtils.isEmpty(nick_name))
            params.put("nick_name", nick_name);
        if (!TextUtils.isEmpty(face))
            params.put("face", face);
        if (!TextUtils.isEmpty(password))
            params.put("password", password);

        Map<String, String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(UserInfoHelper.getToken()))
            headers.put("Authorization", "Bearer " + UserInfoHelper.getToken());
        return HttpCoreEngin.get(context).rxpost(NetConstant.user_update_url, new TypeReference<ResultInfo<UserInfo>>() {
        }.getType(), params, headers, false, false, false);
    }


    /**
     * 上传图像
     *
     * @param file
     * @param fileName
     * @return
     */
    public static Observable<ResultInfo<UploadInfo>> uploadInfo(Context context, File file, String fileName) {

        UpFileInfo upFileInfo = new UpFileInfo();
        upFileInfo.file = file;
        upFileInfo.filename = fileName;
        upFileInfo.name = "image";
        Map<String, String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(UserInfoHelper.getToken()))
            headers.put("Authorization", "Bearer " + UserInfoHelper.getToken());
        return HttpCoreEngin.get(context).rxuploadFile(NetConstant.upload_url, new TypeReference<ResultInfo<UploadInfo>>() {
        }.getType(), upFileInfo, null, headers, false);

    }


    public static Observable<ResultInfo<List<String>>> getConditionList(Context context) {

        Map<String, String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(UserInfoHelper.getToken()))
            headers.put("Authorization", "Bearer " + UserInfoHelper.getToken());
        return HttpCoreEngin.get(context).rxpost(NetConstant.book_tag_url, new TypeReference<ResultInfo<List<String>>>() {
        }.getType(), null, headers, false, false, false);

    }


    public static Observable<ResultInfo<ShareInfo>> getShareInfo(Context context) {
        Map<String, String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(UserInfoHelper.getToken()))
            headers.put("Authorization", "Bearer " + UserInfoHelper.getToken());
        return HttpCoreEngin.get(context).rxpost(NetConstant.user_share_url, new TypeReference<ResultInfo<ShareInfo>>() {
        }.getType(), null, headers, false, false, false);
    }

    /**
     * 好评
     *
     * @return
     */
    public static Observable<ResultInfo<String>> comment(Context context, String userid) {
        Map<String, String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(UserInfoHelper.getToken()))
            headers.put("Authorization", "Bearer " + UserInfoHelper.getToken());
        return HttpCoreEngin.get(context).rxpost(NetConstant.task_good_comment_url, new TypeReference<ResultInfo<String>>() {
                }.getType(),
                null,headers, false, false, false);
    }


    /**
     * 获取Q币数量
     *
     * @param context
     * @return
     */
    public static Observable<ResultInfo<QbInfoWrapper>> getQbInfo(Context context) {

        Map<String, String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(UserInfoHelper.getToken()))
            headers.put("Authorization", "Bearer " + UserInfoHelper.getToken());
        return HttpCoreEngin.get(context).rxpost(NetConstant.user_qb_url, new TypeReference<ResultInfo<QbInfoWrapper>>() {
                }.getType(),
                null, headers, false, false, false);
    }


    public static Observable<ResultInfo<TaskLisInfoWrapper>> getTaskInfoList(Context context) {

        Map<String, String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(UserInfoHelper.getToken()))
            headers.put("Authorization", "Bearer " + UserInfoHelper.getToken());

        return HttpCoreEngin.get(context).rxpost(NetConstant.task_list_url, new TypeReference<ResultInfo<TaskLisInfoWrapper>>() {
        }.getType(), null,headers, false, false, false);

    }

}

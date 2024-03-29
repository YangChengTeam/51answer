package com.yc.ac.constant;


import yc.com.base.BaseNetConstant;

/**
 * Created by wanglin  on 2018/3/7 14:38.
 */

public interface NetConstant {

    /**
     * 上传图片
     */
    String upload_url = BaseNetConstant.getBaseUrl() + "upload";

    /**
     * 发送验证码
     */
    String user_code_url = BaseNetConstant.getBaseUrl() + "user/code";

    /**
     * 注册
     */
    String user_reg_url = BaseNetConstant.getBaseUrl() + "user/reg";
    /**
     * 登录
     */
    String user_login_url = BaseNetConstant.getBaseUrl() + "user/login";
    /**
     * 用户资料
     */
    String user_info_url = BaseNetConstant.getBaseUrl() + "user/info";

    /**
     * 找回密码
     */
    String user_reset_url = BaseNetConstant.getBaseUrl() + "user/reset";

    /**
     * 更新资料
     */
    String user_update_url = BaseNetConstant.getBaseUrl() + "user/update";

    /**
     * 设置用户名
     */
    String user_username_url = BaseNetConstant.getBaseUrl() + "user/username";
    /**
     * 修改手机
     */
    String user_rebind_url = BaseNetConstant.getBaseUrl() + "user/rebind";

    /**
     * 绑定手机
     */
    String user_bind_url = BaseNetConstant.getBaseUrl() + "user/bind";

    /**
     * 第三方登录
     */
    String user_sns_url = BaseNetConstant.getBaseUrl() + "user/sns";

    /**
     * 书本版本列表
     */
    String book_version_url = BaseNetConstant.getBaseUrl() + "book/version";
    /**
     * 书本列表
     **/
    String book_index_url = BaseNetConstant.getBaseUrl() + "book/index";
    /**
     * 书本答案
     */
    String book_answer_url = BaseNetConstant.getBaseUrl() + "book/answer";

    /**
     * 分享书本
     */
    String book_share_url = BaseNetConstant.getBaseUrl() + "book/share";
    /**
     * 收藏书本
     */
    String book_favorite_url = BaseNetConstant.getBaseUrl() + "book/favorite";
    /**
     * 收藏列表
     */
    String book_myfavorite_url = BaseNetConstant.getBaseUrl() + "book/myfavorite";

    /**
     * 幻灯展示
     */
    String slide_index_url = BaseNetConstant.getBaseUrl() + "slide/index";


    /**
     * 热门搜索
     */
    String book_tag_url = BaseNetConstant.getBaseUrl() + "book/tag";

    /**
     * 用户分享
     */

    String user_share_url = BaseNetConstant.getBaseUrl() + "user/share";

    /**
     * 搜索下拉
     */
    String book_tip_url = BaseNetConstant.getBaseUrl() + "book/tip";

    /**
     * 阿里云获取AccessKeyId SecretKeyId SecurityToken
     */
    String oss_api_url = "http://answer.bshu.com/api/ali_oss/osstmpkey";

    String user_img_upload_book_url = BaseNetConstant.getBaseUrl() + "user_img/upload_book";

    /**
     * 上传新书的列表
     */
    String upload_list_url = BaseNetConstant.getBaseUrl() + "user_img/check_list";

    /**
     * 用户反馈
     */
    String user_img_feedback_url = BaseNetConstant.getBaseUrl() + "user_img/feedback";

    /**
     * 用户任务列表
     */
    String task_list_url = BaseNetConstant.getBaseUrl() + "task/task_list";
    /**
     * 分享获取任务奖励
     */
    String task_share_reward_url = BaseNetConstant.getBaseUrl() + "task/share_reward";

    /**
     * 好评获取奖励
     */
    String task_good_comment_url = BaseNetConstant.getBaseUrl() + "task/good_comment";

    /**
     * 获取Q币
     */
    String user_qb_url = BaseNetConstant.getBaseUrl() + "user/qb";
    /**
     * 首页tag标签
     */
    String index_tag_url = BaseNetConstant.getBaseUrl() + "tag/index";

    /**
     * 首页专题
     */
    String index_zt_url = BaseNetConstant.getBaseUrl() + "zt/index";

    /**
     * 打开广告开关
     */
    String switch_ad_url = BaseNetConstant.getBaseUrl() + "versionSwitch";

    /**
     * 支付item
     */
    String pay_item_list_url = BaseNetConstant.getBaseUrl() + "pay/goodsList";

    /**
     * 支付宝支付
     */
    String alipay_url = BaseNetConstant.getBaseUrl() + "pay/aliPay";

    /**
     * 微信支付
     */
    String wxpay_url = BaseNetConstant.getBaseUrl() + "pay/wechatPay";


    /**
     * 充值记录列表
     */
    String order_list_url = BaseNetConstant.getBaseUrl() + "pay/orderList";

    /**
     * 用户注销接口
     */
    String cancellation_url = BaseNetConstant.getBaseUrl() + "user/cancellation";
}

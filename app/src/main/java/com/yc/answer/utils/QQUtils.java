package com.yc.answer.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by wanglin  on 2018/3/15 11:42.
 */

public class QQUtils {

    /****************
     *
     * 发起添加群流程。群号：说说英语交流二群(524651117) 的 key 为： wuzu_LXD28r_DJy7INWx-F4WkuhtzDhE
     * 调用 joinQQGroup(wuzu_LXD28r_DJy7INWx-F4WkuhtzDhE) 即可发起手Q客户端申请加群 说说英语交流二群(524651117)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public static boolean joinQQZhongXueGroup(Context context, String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            ToastUtils.showCenterToast(context, "未安装手Q或安装的版本不支持");
            return false;
        }
    }
}

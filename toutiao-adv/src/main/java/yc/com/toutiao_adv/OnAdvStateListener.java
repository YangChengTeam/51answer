package yc.com.toutiao_adv;

import com.bytedance.sdk.openadsdk.TTNativeExpressAd;

import java.util.Map;

/**
 * Created by suns  on 2020/1/6 11:21.
 */
public interface OnAdvStateListener {

    void loadSuccess();

    void loadFailed();

    void clickAD();

    void onTTNativeExpressed(Map<TTNativeExpressAd, Integer> mDatas);

    void onNativeExpressDismiss(TTNativeExpressAd ad);

}

package yc.com.toutiao_adv;

import android.app.Activity;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by suns  on 2020/1/6 11:50.
 */
public class NativeAdManager implements OnAdvManagerListener, WeakHandler.IHandler {

    private OnAdvStateListener stateListener;
    private int mNativeCount;
    private String mNativeAd;
    private Activity mActivity;
    private TTAdNative mTTAdNative;

    private long startTime;
    private List<TTNativeExpressAd> mDatas;
    private List<Integer> mPositions;

    private HashMap<TTNativeExpressAd, Integer> mAdViewPositionMap = new HashMap<>();

    private WeakHandler mHandler = new WeakHandler(this);

    private final int rend_success = 100;
    private final int ad_close = 1;

    public NativeAdManager(Activity activity, String nativeAd, int nativeCount, List<Integer> positions, OnAdvStateListener listener) {
        this.mActivity = activity;

        this.mNativeAd = nativeAd;
        this.mNativeCount = nativeCount;
        this.stateListener = listener;
        this.mPositions = positions;
        mDatas = new ArrayList<>();
    }

    @Override
    public void showAD() {
//step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(mActivity);
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(mActivity);
        loadExpressAd(mNativeAd, mNativeCount);
    }

    private void loadExpressAd(String codeId, int nativeCount) {

        float expressViewWidth = 320;
        float expressViewHeight = 0;

        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(nativeCount) //请求广告数量为1到3条

                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
                .setImageAcceptedSize(640, 320)//这个参数设置即可，不影响模板广告的size
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {

            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
//                mTTAd = ads.get(0);
//                mDatas.clear();
                mDatas = ads;
                mAdViewPositionMap.clear();
                bindAdListener(ads);
                startTime = System.currentTimeMillis();
//                mTTAd.render();
            }
        });
    }

    private void bindAdListener(List<TTNativeExpressAd> ads) {


        for (int i = 0; i < ads.size(); i++) {
            final TTNativeExpressAd ad = ads.get(i);
            mAdViewPositionMap.put(ad, mPositions.get(i));

            ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {

                @Override
                public void onAdClicked(View view, int type) {

                }

                @Override
                public void onAdShow(View view, int type) {
                    Log.e("ExpressView", "onAdShow: " + (System.currentTimeMillis() - startTime));
                }

                @Override
                public void onRenderFail(View view, String msg, int code) {
                    Log.e("ExpressView", "render fail:" + (System.currentTimeMillis() - startTime));

                }

                @Override
                public void onRenderSuccess(View view, float width, float height) {
                    Log.e("ExpressView", "render suc:" + (System.currentTimeMillis() - startTime));
                    //返回view的宽高 单位 dp
//                    mDatas.add(ad);
                    mHandler.sendEmptyMessage(rend_success);
                }
            });
            ad.render();


            if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                return;
            }

            ad.setDislikeCallback(mActivity, new TTAdDislike.DislikeInteractionCallback() {
                @Override
                public void onSelected(int position, String value) {
//                    TToast.show(mContext, "点击 " + value);
                    //用户选择不喜欢原因后，移除广告展示
//                    mData.remove(ad);
//                    notifyDataSetChanged();
                    Message message = mHandler.obtainMessage();
                    message.obj = ad;
                    message.what = ad_close;
                    mHandler.sendMessage(message);
                }

                @Override
                public void onCancel() {
                    Toast.makeText(mActivity, "点击取消 ", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStop() {

    }


    @Override
    public void onDestroy() {
        if (mDatas != null) {
            for (TTNativeExpressAd ad : mDatas) {
                if (ad != null) {
                    ad.destroy();
                }
            }

        }

    }

    @Override
    public void handleMsg(Message msg) {
        if (msg.what == rend_success) {
            stateListener.onTTNativeExpressed(mAdViewPositionMap);
        } else if (msg.what == ad_close) {
            TTNativeExpressAd ad = (TTNativeExpressAd) msg.obj;
            stateListener.onNativeExpressDismiss(ad);
        }
    }
}

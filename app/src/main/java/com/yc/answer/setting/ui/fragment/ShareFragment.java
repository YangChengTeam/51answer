package com.yc.answer.setting.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.share.UMShareImpl;
import com.kk.utils.LogUtil;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.vondear.rxtools.RxSPTool;
import com.yc.answer.R;
import com.yc.answer.constant.SpConstant;
import com.yc.answer.setting.contract.ShareContract;
import com.yc.answer.setting.model.bean.ShareInfo;
import com.yc.answer.setting.presenter.SharePresenter;
import com.yc.answer.utils.ToastUtils;
import com.yc.answer.utils.UserInfoHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseBottomSheetDialogFragment;

/**
 * Created by wanglin  on 2018/3/15 14:32.
 */

public class ShareFragment extends BaseBottomSheetDialogFragment<SharePresenter> implements ShareContract.View {

    @BindView(R.id.ll_wx)
    LinearLayout llWx;
    @BindView(R.id.ll_circle)
    LinearLayout llCircle;
    @BindView(R.id.ll_qq)
    LinearLayout llQq;
    @BindView(R.id.tv_cancel_share)
    TextView tvCancelShare;

    private ShareInfo mShareInfo;
    private boolean mIsShareMoney;


    public void setIsShareMoney(boolean isShareMoney) {
        this.mIsShareMoney = isShareMoney;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_share;
    }

    @Override
    public void init() {
        mPresenter = new SharePresenter(getActivity(), this);
        RxView.clicks(tvCancelShare).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });
        final List<View> shareItemViews = new ArrayList<>();
        shareItemViews.add(llWx);
        shareItemViews.add(llCircle);
        shareItemViews.add(llQq);

        for (int i = 0; i < shareItemViews.size(); i++) {
            View view = shareItemViews.get(i);
            view.setTag(i);
            final int finalI = i;
            RxView.clicks(view).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    shareInfo(finalI);
                }
            });
        }

    }


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            loadingView.setMessage("正在分享...");
            loadingView.show();

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {

            loadingView.dismiss();
            ToastUtils.showCenterToast(mContext, "分享成功");
            RxSPTool.putBoolean(mContext, SpConstant.SHARE_SUCCESS, true);
            if (mShareInfo != null) {
                if (mIsShareMoney) {
                    mPresenter.shareMoney();
                } else {
                    if (!TextUtils.isEmpty(mShareInfo.getBook_id())) {
                        mPresenter.share(mShareInfo);
                    }
                }
            }


        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            loadingView.dismiss();
            ToastUtils.showCenterToast(mContext, "分享有误");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            loadingView.dismiss();
            ToastUtils.showCenterToast(mContext, "取消发送");
        }
    };

    public SHARE_MEDIA getShareMedia(String tag) {
        if (tag.equals("0")) {
            return SHARE_MEDIA.WEIXIN;
        }

        if (tag.equals("1")) {
            return SHARE_MEDIA.WEIXIN_CIRCLE;
        }

        if (tag.equals("2")) {
            return SHARE_MEDIA.QQ;
        }

        return SHARE_MEDIA.WEIXIN;
    }


    private void shareInfo(int tag) {
        String title = "作业必杀技，拯救水深火热中的父母！（看完眼泪都笑出来，附不传之秘）";
        String url = "http://mp.weixin.qq.com/s/YYp1jP8oiOmLFnuLJqUplg";
        String desc = "51答案APP汇总全国各地中小学生各科各年级试卷、教辅、练习册、作业答案等学习内容，为学生们提供不同思路的题目讲解及答案，覆盖了义务教育一到九年级所有课程科目。为家长检查作业提供答案服务，为中小学生提供了一个汇总答案，讨论，学习知识的交流分享平台。";

        if (mShareInfo != null) {
            if (!TextUtils.isEmpty(mShareInfo.getUrl())) {
                url = mShareInfo.getUrl();
            }
            if (!TextUtils.isEmpty(mShareInfo.getTitle())) {
                title = mShareInfo.getTitle();
            }
            if (!TextUtils.isEmpty(mShareInfo.getContent())) {
                desc = mShareInfo.getContent();
            }

        }
        dismiss();
        UMShareImpl.get().setCallback(mContext, umShareListener).shareUrl(title, url, desc, R.mipmap.logo, getShareMedia(tag + ""));

    }


    @Override
    public void showSuccess() {

    }


    public ShareInfo getmShareInfo() {
        return mShareInfo;
    }

    public void setShareInfo(ShareInfo mShareInfo) {
        this.mShareInfo = mShareInfo;
    }
}

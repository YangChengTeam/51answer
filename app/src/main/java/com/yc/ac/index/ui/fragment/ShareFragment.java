package com.yc.ac.index.ui.fragment;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.share.UMShareImpl;
import com.kk.utils.LogUtil;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.vondear.rxtools.RxDeviceTool;
import com.vondear.rxtools.RxPermissionsTool;
import com.yc.ac.R;
import com.yc.ac.index.model.bean.BookInfo;
import com.yc.ac.setting.contract.ShareContract;
import com.yc.ac.setting.model.bean.ShareInfo;
import com.yc.ac.setting.presenter.SharePresenter;
import com.yc.ac.utils.GlideHelper;
import com.yc.ac.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.functions.Action1;
import yc.com.base.BaseDialogFragment;

/**
 * Created by wanglin  on 2018/5/20 09:36.
 */

public class ShareFragment extends BaseDialogFragment<SharePresenter> implements ShareContract.View {
    @BindView(R.id.iv_book_cover)
    ImageView ivBookCover;
    @BindView(R.id.tv_answer_title)
    TextView tvAnswerTitle;
    @BindView(R.id.tv_answer_extra)
    TextView tvAnswerExtra;
    @BindView(R.id.iv_qq)
    ImageView ivQq;
    @BindView(R.id.iv_wx)
    ImageView ivWx;

    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.ll_share)
    LinearLayout llShare;
    private ShareInfo mShareInfo;
    private BookInfo bookInfo;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_share_code;
    }


    @Override
    public void init() {
        if (getArguments() != null) {
            bookInfo = getArguments().getParcelable("bookInfo");
            tvAnswerTitle.setText(bookInfo.getName());
            tvAnswerExtra.setText(String.format(getString(R.string.share_answer_extra), bookInfo.getSubject(), bookInfo.getGrade(), bookInfo.getPart_type()));
            GlideHelper.loadImage(getActivity(), bookInfo.getCover_img(), ivBookCover);
        }

        mPresenter = new SharePresenter(getActivity(), this);

        final List<View> shareItemViews = new ArrayList<>();
        shareItemViews.add(ivQq);
        shareItemViews.add(ivWx);

        for (int i = 0; i < shareItemViews.size(); i++) {
            View view = shareItemViews.get(i);
            view.setTag(i);
            final int finalI = i;
            RxView.clicks(view).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
                llShare.setVisibility(View.GONE);
                ivCode.setVisibility(View.VISIBLE);
                ivCode.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDialog().getWindow().getDecorView().setDrawingCacheEnabled(true);
                        Bitmap bmp = getDialog().getWindow().getDecorView().getDrawingCache();
                        mShareInfo = new ShareInfo();
                        mShareInfo.setBook_id(bookInfo.getBookId());
                        mShareInfo.setBitmap(bmp);


                        shareInfo(finalI);

                    }
                }, 500);

            });
        }

    }


    @Override
    protected float getWidth() {
        return 0.7f;
    }

    @Override
    public int getAnimationId() {
        return R.style.share_anim;
    }

    @Override
    public int getHeight() {
        return RxDeviceTool.getScreenHeight(getActivity()) / 2;
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

            if (mShareInfo != null && !TextUtils.isEmpty(mShareInfo.getBook_id())) {
                mPresenter.share(mShareInfo);
            }

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            loadingView.dismiss();
            LogUtil.msg("err: " + throwable.getMessage());
            ToastUtils.showCenterToast(mContext, "分享有误");
            llShare.setVisibility(View.VISIBLE);
            ivCode.setVisibility(View.GONE);

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            loadingView.dismiss();
            ToastUtils.showCenterToast(mContext, "取消发送");
            llShare.setVisibility(View.VISIBLE);
            ivCode.setVisibility(View.GONE);
        }
    };

    public SHARE_MEDIA getShareMedia(String tag) {
        if (tag.equals("0")) {
            return SHARE_MEDIA.QQ;
        }

        if (tag.equals("1")) {
            return SHARE_MEDIA.WEIXIN;
        }

        if (tag.equals("2")) {
            return SHARE_MEDIA.WEIXIN_CIRCLE;
        }

        return SHARE_MEDIA.QQ;
    }


    private void shareInfo(int tag) {

        if (mShareInfo != null && mShareInfo.getBitmap() != null) {
            UMShareImpl.get().setCallback(mContext, umShareListener).shareImage("hello", mShareInfo.getBitmap(), getShareMedia(tag + ""));
        }
    }

    @Override
    public void showSuccess() {
        dismiss();
    }


}

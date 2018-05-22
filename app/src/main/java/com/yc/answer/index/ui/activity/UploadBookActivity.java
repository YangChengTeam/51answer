package com.yc.answer.index.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxPhotoTool;
import com.yalantis.ucrop.UCrop;
import com.yc.answer.R;
import com.yc.answer.constant.BusAction;
import com.yc.answer.index.contract.UploadContract;
import com.yc.answer.index.model.bean.BookInfo;
import com.yc.answer.index.presenter.UploadPresenter;
import com.yc.answer.index.ui.fragment.PhotoFragment;
import com.yc.answer.utils.GlideHelper;
import com.yc.answer.utils.IvAvatarHelper;
import com.yc.answer.utils.ToastUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/4/20 10:53.
 */

public class UploadBookActivity extends BaseActivity<UploadPresenter> implements UploadContract.View {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;

    @BindView(R.id.iv_your_cover)
    ImageView ivYourCover;

    @BindView(R.id.tv_next)
    TextView tvNext;
    private String path;

    @Override
    public int getLayoutId() {
        return R.layout.activity_upload_book;
    }

    @Override
    public void init() {
        mPresenter = new UploadPresenter(this, this);
        commonTvTitle.setText(getString(R.string.upload_book));

        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        RxView.clicks(ivYourCover).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                PhotoFragment photoFragment = new PhotoFragment();
                photoFragment.show(getSupportFragmentManager(), "");
                photoFragment.setOnCameraPickListener(new PhotoFragment.onCameraPickListener() {
                    @Override
                    public void onOpenCamera() {
                        RxPhotoTool.openCameraImage(UploadBookActivity.this);
                    }

                    @Override
                    public void onOpenImage() {
                        RxPhotoTool.openLocalImage(UploadBookActivity.this);
                    }
                });
            }
        });
        RxView.clicks(tvNext).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (bookInfo == null || TextUtils.isEmpty(bookInfo.getCover_img())) {
                    ToastUtils.showCenterToast(UploadBookActivity.this, "请先上传书本封面");
                    return;
                }
                Intent intent = new Intent(UploadBookActivity.this, UploadAnswerActivity.class);
                intent.putExtra("cover_img", bookInfo.getCover_img());
                intent.putExtra("cover_id", bookInfo.getCover_id());
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RxPhotoTool.GET_IMAGE_FROM_PHONE://选择相册之后的处理
                if (resultCode == RESULT_OK) {
//                    RxPhotoTool.cropImage(MainActivity.this,data.getData() );// 裁剪图片
                    IvAvatarHelper.initUCrop(UploadBookActivity.this, data.getData());
                }

                break;
            case RxPhotoTool.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
                if (resultCode == RESULT_OK) {
                   /* data.getExtras().get("data");*/
//                    RxPhotoTool.cropImage(ActivityUser.this, RxPhotoTool.imageUriFromCamera);// 裁剪图片
                    IvAvatarHelper.initUCrop(UploadBookActivity.this, RxPhotoTool.imageUriFromCamera);
                }

                break;
            case RxPhotoTool.CROP_IMAGE://普通裁剪后的处理
                path = RxPhotoTool.getImageAbsolutePath(UploadBookActivity.this, RxPhotoTool.cropImageUri);

                GlideHelper.loadImage(UploadBookActivity.this, path, ivYourCover, R.mipmap.add_cover);
                break;

            case UCrop.REQUEST_CROP://UCrop裁剪之后的处理
                if (resultCode == RESULT_OK) {
                    Uri resultUri = UCrop.getOutput(data);
                    path = RxPhotoTool.getImageAbsolutePath(UploadBookActivity.this, resultUri);
                    mPresenter.getOssInfo(path, "cover", "");
//                    GlideImageLoader.loadImage(UploadBookIntroduceActivity.this, path, ivYourCover, R.mipmap.add_cover);
                }
                break;
        }

    }

    private BookInfo bookInfo;

    @Override
    public void showUploadResult(BookInfo body) {
        bookInfo = body;
        GlideHelper.loadImage(this, body.getCover_img(), ivYourCover, R.mipmap.add_cover);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.FINISH)
            })
    public void finishSelf(String finish) {
        finish();
    }
}

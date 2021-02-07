package com.yc.ac.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import com.hwangjr.rxbus.RxBus;
import com.vondear.rxtools.RxPhotoTool;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.yc.ac.R;
import com.yc.ac.constant.BusAction;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import static android.app.Activity.RESULT_OK;

/**
 * Created by wanglin  on 2018/3/15 08:57.
 */

public class IvAvatarHelper {

    public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RxPhotoTool.GET_IMAGE_FROM_PHONE://选择相册之后的处理
                if (resultCode == RESULT_OK) {
                    RxPhotoTool.cropImage(activity, data.getData(), RxPhotoTool.GET_IMAGE_FROM_PHONE);// 裁剪图片
//                    initUCrop(activity, data.getData());
                }

                break;
            case RxPhotoTool.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
                if (resultCode == RESULT_OK) {

                    RxPhotoTool.cropImage(activity, RxPhotoTool.imageUriFromCamera, RxPhotoTool.GET_IMAGE_BY_CAMERA);// 裁剪图片
                }

//                    initUCrop(activity, RxPhotoTool.imageUriFromCamera);

                break;
            case RxPhotoTool.CROP_IMAGE://普通裁剪后的处理
//                    roadImageView(RxPhotoTool.cropImageUri, ivAvatar);
                RxBus.get().post(BusAction.GET_PICTURE, RxPhotoTool.cropImageUri);
                break;

            case UCrop.REQUEST_CROP://UCrop裁剪之后的处理
                if (resultCode == RESULT_OK) {
                    Uri resultUri = UCrop.getOutput(data);
//                       roadImageView(resultUri, ivAvatar);
                    RxBus.get().post(BusAction.GET_PICTURE, resultUri);
                }
                break;
        }
    }


    public static void initUCrop(Activity activity, Uri uri) {
        //Uri destinationUri = RxPhotoTool.createImagePathUri(this);

        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));


        Uri destinationUri= Uri.fromFile(new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), imageName + ".png"));
//        Uri destinationUri = getUriForFile(activity, new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), imageName + ".png"));

        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置隐藏底部容器，默认显示
        //options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(activity, R.color.colorPrimaryDark));

        //开始设置
        //设置最大缩放比例
        options.setMaxScaleMultiplier(5);
        //设置图片在切换比例时的动画
        options.setImageToCropBoundsAnimDuration(666);
        //设置裁剪窗口是否为椭圆
        //options.setOvalDimmedLayer(true);
        //设置是否展示矩形裁剪框
        // options.setShowCropFrame(false);
        //设置裁剪框横竖线的宽度
        //options.setCropGridStrokeWidth(20);
        //设置裁剪框横竖线的颜色
        //options.setCropGridColor(Color.GREEN);
        //设置竖线的数量
        //options.setCropGridColumnCount(2);
        //设置横线的数量
        //options.setCropGridRowCount(1);

        UCrop.of(uri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(500, 500)
                .withOptions(options)
                .start(activity);
    }

    //兼容7.0
    private static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, getFileProviderName(context), file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    private static String getFileProviderName(Context context) {
        return context.getPackageName() + ".fileprovider";
    }
}

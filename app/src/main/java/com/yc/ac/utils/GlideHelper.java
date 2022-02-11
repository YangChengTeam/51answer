package com.yc.ac.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yc.ac.R;

/**
 * Created by wanglin  on 2018/4/20 15:37.
 */

public class GlideHelper {


    public static void loadImage(Context context, String path, ImageView imageView, int placeHolder, boolean isCenterCrop) {
        RequestOptions options = new RequestOptions();
        options.placeholder(placeHolder).error(placeHolder).diskCacheStrategy(DiskCacheStrategy.DATA).skipMemoryCache(false);
        if (isCenterCrop) {
            options.centerCrop();
        }
        Glide.with(context).load(path).apply(options).thumbnail(0.1f).into(imageView);
    }

    public static void loadImage(Context context, String path, ImageView imageView, int placeHolder) {
        loadImage(context, path, imageView, placeHolder, true);
    }

    public static void loadImage(Context context, String path, ImageView imageView) {
        loadImage(context, path, imageView, R.mipmap.small_placeholder);
    }


}

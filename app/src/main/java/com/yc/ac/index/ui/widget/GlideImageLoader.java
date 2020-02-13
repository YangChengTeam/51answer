package com.yc.ac.index.ui.widget;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.yc.ac.R;

import androidx.annotation.Nullable;
import cn.finalteam.galleryfinal.widget.GFImageView;

/**
 * Created by wanglin  on 2018/4/24 11:41.
 */

public class GlideImageLoader implements cn.finalteam.galleryfinal.ImageLoader {
    @Override
    public void displayImage(Activity activity, String path, final GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        Glide.with(activity)
                .load("file://" + path).apply(new RequestOptions().placeholder(defaultDrawable)
                .error(defaultDrawable)
                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.NONE) //不缓存到SD卡
                .skipMemoryCache(true))

                //.centerCrop()
                .into(new ImageViewTarget<Drawable>(imageView) {
                          @Override
                          protected void setResource(@Nullable Drawable resource) {
                              imageView.setImageDrawable(resource);
                          }

                          @Override
                          public void setRequest(@Nullable Request request) {
                              imageView.setTag(R.id.adapter_item_tag_key, request);
                          }

                          @Nullable
                          @Override
                          public Request getRequest() {
                              return (Request) imageView.getTag(R.id.adapter_item_tag_key);
                          }
                      }
                );
    }

    @Override
    public void clearMemoryCache() {

    }
}

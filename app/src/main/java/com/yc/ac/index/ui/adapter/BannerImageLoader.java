package com.yc.ac.index.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by wanglin  on 2018/3/12 16:47.
 */

public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        try {
//            imageView.setBackgroundColor(Color.parseColor("#e0eaf4"));
            Glide.with(context).load(path).into(imageView);
        } catch (Exception e){
            Log.e("BannerImageLoader",  e.getMessage());
        }
    }
}

package com.yc.ac.index.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.yc.ac.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by wanglin  on 2018/3/12 11:25.
 */

public class AnswerDetailAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> mImageList;
    private List<String> mTitles;

    private boolean isClick = false;

    private boolean isDoubleClick = false;

    public AnswerDetailAdapter(Context context, List<String> imageList) {
        this.mImageList = imageList;
        this.mContext = context;
        mTitles = new ArrayList<>();
        if (imageList != null) {
            for (int i = 0; i < imageList.size(); i++) {
                mTitles.add((i + 1) + "");
            }
        }
    }

    @Override
    public int getCount() {
        return mImageList == null ? 0 : mImageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_answer_detail_item, container, false);

        String path = mImageList.get(position);

        final PhotoView scaleImageView = view.findViewById(R.id.xImageView);

        Glide.with(mContext).asDrawable().load(path).apply(new RequestOptions()
                .placeholder(R.mipmap.big_placeholder).error(R.mipmap.big_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.DATA).skipMemoryCache(true)
                .centerInside()).thumbnail(0.1f).into(new ImageViewTarget<Drawable>(scaleImageView) {
            @Override
            protected void setResource(@Nullable Drawable resource) {
                scaleImageView.setImageDrawable(resource);
            }


        });

//        Glide.with(mContext).asBitmap().load(path).apply(new RequestOptions()
//                .placeholder(R.mipmap.big_placeholder).error(R.mipmap.big_placeholder).diskCacheStrategy(DiskCacheStrategy.DATA).skipMemoryCache(true).centerInside()).thumbnail(0.1f).into(scaleImageView);
        container.addView(view);

        scaleImageView.setOnClickListener(v -> {
            isClick = !isClick;
            if (onViewClickListener != null) {
                onViewClickListener.onViewSingleClick(isClick);
            }
        });
        scaleImageView.setOnTouchImageViewListener(() -> {
            isDoubleClick = !isDoubleClick;
            if (onViewClickListener != null) {
                onViewClickListener.onViewDoubleClick(isDoubleClick);
            }
        });

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    public interface onViewClickListener {
        void onViewSingleClick(boolean isClick);

        void onViewDoubleClick(boolean isClick);

    }

    public onViewClickListener onViewClickListener;


    public void setOnViewClickListener(AnswerDetailAdapter.onViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
    }
}

package com.yc.ac.index.ui.adapter;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bytedance.msdk.api.v2.ad.nativeAd.GMNativeAd;
import com.bytedance.msdk.api.v2.ad.nativeAd.GMNativeExpressAdListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.ac.R;
import com.yc.ac.base.MyApp;
import com.yc.ac.index.model.bean.BookInfo;
import com.yc.ac.utils.SubjectHelper;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wanglin  on 2018/3/10 10:56.
 */

public class SearchResultItemAdapter extends BaseMultiItemQuickAdapter<BookInfo, BaseViewHolder> {
    private HashMap<TTNativeExpressAd, Integer> mAdViewPositionMap;
    private boolean isNeedRe;
    public SearchResultItemAdapter(@Nullable List<BookInfo> data, HashMap<TTNativeExpressAd, Integer> adViewPositionMap) {
        super(data);
        this.mAdViewPositionMap = adViewPositionMap;
        addItemType(BookInfo.ADV, R.layout.item_express_ad);
        addItemType(BookInfo.CONTENT, R.layout.fragment_search_result_item);
    }

    public void addADViewToPosition(int position, BookInfo bookInfo) {
        if (mData != null && position >= 0 && position < mData.size() && bookInfo.getView() != null) {
            mData.add(position, bookInfo);
            notifyDataSetChanged();
        }
    }

    // 移除NativeExpressADView的时候是一条一条移除的
    public void removeADView(int position, TTNativeExpressAd adView) {
        mData.remove(position);
        notifyItemRemoved(position); // position为adView在当前列表中的位置
    }

    @Override
    protected void convert(BaseViewHolder helper, BookInfo item) {
        switch (helper.getItemViewType()) {
            case BookInfo.ADV:
                final TTNativeExpressAd adView = item.getView();

                mAdViewPositionMap.put(adView, helper.getAdapterPosition()); // 广告在列表中的位置是可以被更新的
                FrameLayout container = helper.getView(R.id.express_ad_container);
                if (container.getChildCount() > 0
                        && container.getChildAt(0) == adView) {
                    return;
                }

                if (container.getChildCount() > 0) {
                    container.removeAllViews();
                }

                if (adView.getExpressAdView().getParent() != null) {
                    ((ViewGroup) adView.getExpressAdView().getParent()).removeView(adView.getExpressAdView());
                }
//                nativeExpressAd.render(); // 调用render方法后sdk才会开始展示广告
                container.addView(adView.getExpressAdView());
                break;
            case BookInfo.CONTENT:
                helper.setText(R.id.tv_book_title, item.getName()).setText(R.id.tv_grade, item.getGrade())
                        .setText(R.id.tv_part, item.getPart_type()).setText(R.id.tv_version, item.getVersion()).addOnClickListener(R.id.tv_collect);
                helper.setBackgroundRes(R.id.tv_collect, item.getFavorite() == 1 ? R.drawable.book_collect_gray_bg : R.drawable.book_collect_red_bg);
                helper.setText(R.id.tv_collect, item.getFavorite() == 1 ? "已收藏" : "收藏");
                helper.setVisible(R.id.iv_vip, item.getIsVip() == 1);

                Glide.with(mContext).load(item.getCover_img()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)
                        .skipMemoryCache(true).centerCrop().error(R.mipmap.small_placeholder).dontAnimate()).thumbnail(0.1f).into((ImageView) helper.getView(R.id.iv_book));

                SubjectHelper.setSubject(helper, item, R.id.iv_subject);
                LinearLayout adLine = (LinearLayout) helper.getView(R.id.line_exAd);


                adLine.setVisibility(View.GONE);
                if (MyApp.state==1){
                    if (helper.getAdapterPosition()==1){
                        if (ads!=null&&ads.size()>0){
                            if (ads.get(0)!=null) {
                                FrameLayout fl_exConstain = (FrameLayout) helper.getView(R.id.fl_exConstain);
                                if (fl_exConstain.getVisibility()==View.GONE){
                                    adLine.setVisibility(View.VISIBLE);
                                    getExpressAdView(ads.get(0),fl_exConstain);
                                }
                            }
                        }
                    }

                    if (helper.getAdapterPosition()==3){
                        if (ads!=null&&ads.size()>1){
                            if (ads.get(1)!=null) {
                                FrameLayout fl_exConstain = (FrameLayout) helper.getView(R.id.fl_exConstain);
                                if (fl_exConstain.getVisibility()==View.GONE){
                                    adLine.setVisibility(View.VISIBLE);
                                    getExpressAdView(ads.get(1),fl_exConstain);
                                }
                            }
                        }
                    }

                    if (helper.getAdapterPosition()==6){
                        if (ads!=null&&ads.size()>2){
                            if (ads.get(2)!=null) {
                                FrameLayout fl_exConstain = (FrameLayout) helper.getView(R.id.fl_exConstain);
                                if (fl_exConstain.getVisibility()==View.GONE){
                                    adLine.setVisibility(View.VISIBLE);
                                    getExpressAdView(ads.get(2),fl_exConstain);
                                }
                            }
                        }
                    }
                }


                break;
        }


//        List<VersionDetailInfo> flag = item.getFlag();
//        if (flag == null || flag.size() == 0) {
//            flag = new ArrayList<>();
//            flag.add(new VersionDetailInfo("", item.getVersion()));
//        }
//
//        MyRecyclerView recyclerView = helper.getView(R.id.tag_recyclerView);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
//
//        DetailFlagAdapter detailFlagAdapter = new DetailFlagAdapter(flag, false);
//        recyclerView.setAdapter(detailFlagAdapter);

    }


    //渲染模板广告
    @SuppressWarnings("RedundantCast")
    private View getExpressAdView(final GMNativeAd ad,FrameLayout fl_excontains) {
        View convertView = null;
        try {
            //判断是否存在dislike按钮
            ad.setNativeAdListener(new GMNativeExpressAdListener() {
                @Override
                public void onRenderFail(View view, String s, int i) {

                }

                @Override
                public void onRenderSuccess(float width, float height) {

                    //回调渲染成功后将模板布局添加的父View中
                    //获取视频播放view,该view SDK内部渲染，在媒体平台可配置视频是否自动播放等设置。
                    int sWidth;
                    int sHeight;
                    /**
                     * 如果存在父布局，需要先从父布局中移除
                     */
                    final View video = ad.getExpressView(); // 获取广告view  如果存在父布局，需要先从父布局中移除
                    /*if (width == TTAdSize.FULL_WIDTH && height == TTAdSize.AUTO_HEIGHT) {
                        sWidth = FrameLayout.LayoutParams.MATCH_PARENT;
                        sHeight = FrameLayout.LayoutParams.WRAP_CONTENT;
                    } else {
                        sWidth = UIUtils.getScreenWidth(MainActivity.this);
                        sHeight = (int) ((sWidth * height) / width);
                    }
                    if (exType==1){
                        if (fl_excontains != null) {
                            Log.d("ccc", "-----ex---turn-----555----onRenderSuccess: ");
                            if (video != null) {
                                *//**
                     * 如果存在父布局，需要先从父布局中移除
                     *//*
                                Log.d("ccc", "-----ex---turn-----666----onRenderSuccess: ");
                                UIUtils.removeFromParent(video);
                                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(sWidth, sHeight);
                                fl_excontains.removeAllViews();
                                fl_excontains.addView(video, layoutParams);
                            }
                        }
                    }else {
                        if (fl_exConstainss != null) {
                            Log.d("ccc", "-----ex---turn-----555----onRenderSuccess: ");
                            if (video != null) {
                                *//**
                     * 如果存在父布局，需要先从父布局中移除
                     *//*
                                Log.d("ccc", "-----ex---turn-----666----onRenderSuccess: ");
                                UIUtils.removeFromParent(video);
                                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(sWidth, sHeight);
                                fl_exConstainss.removeAllViews();
                                fl_exConstainss.addView(video, layoutParams);
                            }
                        }
                    }*/


                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = Gravity.CENTER;
                    if (fl_excontains != null) {
                        fl_excontains.removeAllViews();
                        fl_excontains.addView(video,layoutParams);
                    }
                }

                @Override
                public void onAdClick() {

                }

                @Override
                public void onAdShow() {

                }
            });

            ad.render();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private List<GMNativeAd> ads;
    public void setExData(List<GMNativeAd> adss) {
       this.ads=adss;
    }
}

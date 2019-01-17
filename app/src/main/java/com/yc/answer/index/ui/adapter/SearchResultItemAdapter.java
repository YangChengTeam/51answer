package com.yc.answer.index.ui.adapter;

import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.yc.answer.R;
import com.yc.answer.index.model.bean.BookInfo;
import com.yc.answer.utils.SubjectHelper;

import java.util.HashMap;
import java.util.List;

/**
 * Created by wanglin  on 2018/3/10 10:56.
 */

public class SearchResultItemAdapter extends BaseMultiItemQuickAdapter<BookInfo, BaseViewHolder> {
    private HashMap<NativeExpressADView, Integer> mAdViewPositionMap;

    public SearchResultItemAdapter(@Nullable List<BookInfo> data, HashMap<NativeExpressADView, Integer> adViewPositionMap) {
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
    public void removeADView(int position, NativeExpressADView adView) {
        mData.remove(position);
        notifyItemRemoved(position); // position为adView在当前列表中的位置
    }

    @Override
    protected void convert(BaseViewHolder helper, BookInfo item) {

        switch (helper.getItemViewType()) {

            case BookInfo.ADV:
                final NativeExpressADView adView = item.getView();

                mAdViewPositionMap.put(adView, helper.getAdapterPosition()); // 广告在列表中的位置是可以被更新的
                FrameLayout container = helper.getView(R.id.express_ad_container);
                if (container.getChildCount() > 0
                        && container.getChildAt(0) == adView) {
                    return;
                }

                if (container.getChildCount() > 0) {
                    container.removeAllViews();
                }

                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }
                adView.render(); // 调用render方法后sdk才会开始展示广告
                container.addView(adView);

                break;
            case BookInfo.CONTENT:
                helper.setText(R.id.tv_book_title, item.getName()).setText(R.id.tv_grade, item.getGrade())
                        .setText(R.id.tv_part, item.getPart_type()).setText(R.id.tv_version, item.getVersion()).addOnClickListener(R.id.tv_collect);
                helper.setBackgroundRes(R.id.tv_collect, item.getFavorite() == 1 ? R.drawable.book_collect_gray_bg : R.drawable.book_collect_red_bg);
                helper.setText(R.id.tv_collect, item.getFavorite() == 1 ? "已收藏" : "收藏");

                Glide.with(mContext).load(item.getCover_img()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)
                        .skipMemoryCache(true).centerCrop().error(R.mipmap.small_placeholder).dontAnimate()).thumbnail(0.1f).into((ImageView) helper.getView(R.id.iv_book));

                SubjectHelper.setSubject(helper, item, R.id.iv_subject);
                break;
        }


    }


}

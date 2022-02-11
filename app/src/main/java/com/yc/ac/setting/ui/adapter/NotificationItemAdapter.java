package com.yc.ac.setting.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.ac.R;
import com.yc.ac.setting.model.bean.NotificationInfo;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by wanglin on 2021/5/13 14:57
 */
public class NotificationItemAdapter extends BaseQuickAdapter<NotificationInfo, BaseViewHolder> {
    public NotificationItemAdapter(@Nullable List<NotificationInfo> data) {
        super(R.layout.notification_item_view, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NotificationInfo item) {
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        helper.setText(R.id.tv_notification_title, item.getTitle())
                .setText(R.id.tv_date, sm.format(item.getDate()));
    }
}

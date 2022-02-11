package com.yc.ac.setting.ui.adapter;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.ac.R;
import com.yc.ac.setting.model.bean.OrderInfo;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class RechargeRecordAdapter extends BaseQuickAdapter<OrderInfo, BaseViewHolder> {
    public RechargeRecordAdapter(@Nullable List<OrderInfo> data) {
        super(R.layout.recharge_record_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderInfo item) {
//        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        helper.setText(R.id.tv_order_num, "订单号：" + item.getOrderno())
                .setText(R.id.tv_order_title, item.getTitle())
                .setText(R.id.tv_order_money, "¥" + item.getMoney())
                .setText(R.id.tv_order_date, item.getDate())
                .setText(R.id.tv_order_state, item.getStatus() == 2 ? "已支付" : "未支付")
                .setTextColor(R.id.tv_order_state, item.getStatus() == 2 ?
                        ContextCompat.getColor(mContext, R.color.green) : ContextCompat.getColor(mContext, R.color.red_f14343));
    }
}

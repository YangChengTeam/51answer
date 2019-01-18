package com.yc.ac.setting.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kk.utils.LogUtil;
import com.yc.ac.R;
import com.yc.ac.setting.model.bean.TaskListInfo;
import com.yc.ac.setting.ui.widget.BaseSettingView;

import java.util.List;


/**
 * Created by wanglin  on 2018/9/13 17:59.
 */
public class TaskListAdapter extends BaseQuickAdapter<TaskListInfo, BaseViewHolder> {

    private String done = "已完成";
    private String gotoDone = "去完成";

    public TaskListAdapter(@Nullable List<TaskListInfo> data) {
        super(R.layout.item_task_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskListInfo item) {
        try {
            BaseSettingView view = helper.getView(R.id.baseSettingView_new_book);
            view.setTitle(item.getDesp());
            view.setExtraText(item.getIs_done() == 1 ? done : gotoDone);
            view.setExtraColor(item.getIs_done() == 1 ? ContextCompat.getColor(mContext, R.color.green_4ec54e) : ContextCompat.getColor(mContext, R.color.gray_999));
            helper.addOnClickListener(R.id.baseSettingView_new_book);
        } catch (Exception e) {
            LogUtil.msg("exception:  " + e.getMessage());
        }

    }

}

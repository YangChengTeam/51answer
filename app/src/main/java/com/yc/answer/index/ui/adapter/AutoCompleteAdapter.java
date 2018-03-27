package com.yc.answer.index.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.yc.answer.R;

import java.util.List;

/**
 * Created by wanglin  on 2018/3/20 11:29.
 */

public class AutoCompleteAdapter extends ArrayAdapter<String> {


    public AutoCompleteAdapter(@NonNull Context context, List<String> list) {
        super(context, R.layout.auto_complete_item, R.id.tv_auto_complete, list);

    }




}

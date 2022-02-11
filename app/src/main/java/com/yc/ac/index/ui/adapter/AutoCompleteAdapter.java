package com.yc.ac.index.ui.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.yc.ac.R;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by wanglin  on 2018/3/20 11:29.
 */

public class AutoCompleteAdapter extends ArrayAdapter<String> {


    public AutoCompleteAdapter(@NonNull Context context, List<String> list) {
        super(context, R.layout.auto_complete_item, R.id.tv_auto_complete, list);

    }




}

package com.yc.ac.base;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yc.ac.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanglin  on 2018/3/15 16:06.
 */

public class LoadingDialog extends Dialog {


    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.tv_loading_message)
    TextView tvLoadingMessage;


    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.custom_dialog);

        View view = LayoutInflater.from(context).inflate(R.layout.base_loading_view, null);
        ButterKnife.bind(this, view);
        setContentView(view);
    }

    public void setMessage(String title) {
        tvLoadingMessage.setText(title);
    }

    public void show(String title) {
        setMessage(title);
        show();
    }
}

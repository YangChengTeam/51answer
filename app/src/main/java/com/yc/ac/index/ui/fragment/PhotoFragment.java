package com.yc.ac.index.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.vondear.rxtools.RxPhotoTool;
import com.yc.ac.R;
import com.yc.ac.index.ui.adapter.PhotoAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/1/26 10:36.
 */

public class PhotoFragment extends BottomSheetDialogFragment {


    RecyclerView recyclerViewAlarm;

    private List<String> items;
    private Context mContext;
    private View rootView;
    private BottomSheetDialog dialog;
    private BottomSheetBehavior<View> mBehavior;
    private TextView tvCancel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        //这里设置透明度
        windowParams.dimAmount = 0.5f;
//        windowParams.width = (int) (ScreenUtil.getWidth(mContext) * 0.98);
        window.setAttributes(windowParams);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new BottomSheetDialog(getContext(), getTheme());
        if (rootView == null) {
            //缓存下来的 View 当为空时才需要初始化 并缓存
            rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_photo_tint, null);
            recyclerViewAlarm = rootView.findViewById(R.id.recyclerView_alarm);
            tvCancel = rootView.findViewById(R.id.tv_cancel);
        }
        dialog.setContentView(rootView);
        mBehavior = BottomSheetBehavior.from((View) rootView.getParent());
        ((View) rootView.getParent()).setBackgroundColor(Color.TRANSPARENT);
        rootView.post(new Runnable() {
            @Override
            public void run() {
                /**
                 * PeekHeight 默认高度 256dp 会在该高度上悬浮
                 * 设置等于 view 的高 就不会卡住
                 */
                mBehavior.setPeekHeight(rootView.getHeight());
            }
        });
        init();
        return dialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除缓存 View 和当前 ViewGroup 的关联
        ((ViewGroup) (rootView.getParent())).removeView(rootView);
    }


    private void init() {

        items = Arrays.asList(getActivity().getResources().getStringArray(R.array.photo_item));
        recyclerViewAlarm.setLayoutManager(new LinearLayoutManager(getActivity()));
        PhotoAdapter alarmAdapter = new PhotoAdapter(items);
        recyclerViewAlarm.setAdapter(alarmAdapter);

        alarmAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (listener != null) {
                    if (position == 0) {
                        listener.onOpenImage();
                    } else if (position == 1) {
                        listener.onOpenCamera();
                    }
                }
                dismiss();
            }
        });

        RxView.clicks(tvCancel).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });
    }


    public onCameraPickListener listener;

    public void setOnCameraPickListener(onCameraPickListener listener) {
        this.listener = listener;
    }

    public interface onCameraPickListener {
        void onOpenCamera();

        void onOpenImage();
    }
}

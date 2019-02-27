package com.yc.answer.index.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.vondear.rxtools.RxDeviceTool;
import com.yc.answer.R;
import com.yc.answer.base.WebActivity;
import com.yc.answer.setting.model.bean.ShareInfo;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.functions.Action1;
import yc.com.base.BaseDialogFragment;

/**
 * Created by wanglin  on 2019/2/22 16:12.
 */
public class HomeworkGuideFragment extends BaseDialogFragment {
    @BindView(R.id.tv_unlock_read)
    TextView tvUnlockRead;
    @BindView(R.id.iv_close)
    ImageView ivClose;

    @Override
    protected float getWidth() {
        return 0.8f;
    }

    @Override
    public int getAnimationId() {
        return R.style.share_anim;
    }

    @Override
    public int getHeight() {
        return RxDeviceTool.getScreenHeight(getActivity()) / 2;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_homework_guide;
    }

    @Override
    public void init() {
        RxView.clicks(ivClose).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //关闭对话框
                dismiss();
            }
        });

        RxView.clicks(tvUnlockRead).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //todo 暂时换未实现
//                Intent intent = new Intent(getActivity(), WebActivity.class);
////                intent.putExtra("url", "http://m.upkao.com/zylxc.html");
//                intent.putExtra("url", "http://tic.upkao.com/apk/zuoyela.apk");
//                startActivity(intent);

                Uri uri = Uri.parse("http://tic.upkao.com/apk/zuoyela.apk");
                //String android.intent.action.VIEW 比较通用，会根据用户的数据类型打开相应的Activity。如:浏览器,电话,播放器,地图
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


                dismiss();
            }
        });
    }

}

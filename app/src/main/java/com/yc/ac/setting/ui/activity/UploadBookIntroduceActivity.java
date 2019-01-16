package com.yc.ac.setting.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.ac.R;
import com.yc.ac.index.ui.activity.UploadAnswerActivity;
import com.yc.ac.index.ui.activity.UploadBookActivity;
import com.yc.ac.setting.ui.fragment.ShareFragment;
import com.yc.ac.utils.ShareInfoHelper;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/5/21 18:28.
 */

public class UploadBookIntroduceActivity extends BaseActivity {
    @BindView(R.id.btn_share)
    Button btnShare;
    @BindView(R.id.btn_upload)
    Button btnUpload;

    @Override
    public int getLayoutId() {
        return R.layout.activity_upload_book_introduce;
    }

    @Override
    public void init() {

        RxView.clicks(btnUpload).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(UploadBookIntroduceActivity.this, UploadBookActivity.class));
            }
        });
        RxView.clicks(btnShare).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ShareFragment shareFragment = new ShareFragment();
                shareFragment.setShareInfo(ShareInfoHelper.getShareInfo());
                shareFragment.show(getSupportFragmentManager(), "");
            }
        });
    }

}

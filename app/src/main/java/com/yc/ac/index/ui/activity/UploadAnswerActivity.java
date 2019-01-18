package com.yc.ac.index.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.vondear.rxtools.RxPhotoTool;
import com.yalantis.ucrop.UCrop;
import com.yc.ac.R;
import com.yc.ac.constant.BusAction;
import com.yc.ac.index.contract.UploadContract;
import com.yc.ac.index.model.bean.BookInfo;
import com.yc.ac.index.presenter.UploadPresenter;
import com.yc.ac.index.ui.adapter.UploadAnswerAdapter;
import com.yc.ac.index.ui.fragment.PhotoFragment;
import com.yc.ac.index.ui.widget.MyDecoration;
import com.yc.ac.setting.model.bean.UploadInfo;
import com.yc.ac.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import rx.functions.Action1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/4/20 15:02.
 */

public class UploadAnswerActivity extends BaseActivity<UploadPresenter> implements UploadContract.View {
    private static final String TAG = "UploadAnswerActivity";
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;

    @BindView(R.id.upload_answer_recyclerView)
    RecyclerView uploadAnswerRecyclerView;
    @BindView(R.id.tv_next)
    TextView tvNext;
    private List<String> uploadInfos;
    private UploadAnswerAdapter answerAdapter;
    private String bookId;
    private String coverImg;

    @Override
    public int getLayoutId() {
        return R.layout.activity_upload_answer;
    }

    @Override
    public void init() {
        uploadInfos = new ArrayList<>();
        uploadInfos.add("");
        mPresenter = new UploadPresenter(this, this);
        commonTvTitle.setText(getString(R.string.upload_answer));

        coverImg = getIntent().getStringExtra("cover_img");
        bookId = getIntent().getStringExtra("cover_id");

        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        uploadAnswerRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        answerAdapter = new UploadAnswerAdapter(uploadInfos);
        uploadAnswerRecyclerView.setAdapter(answerAdapter);
        uploadAnswerRecyclerView.addItemDecoration(new MyDecoration(10, 10));
        initListener();
    }

    private void initListener() {
        answerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == adapter.getItemCount() - 1) {
                    PhotoFragment photoFragment = new PhotoFragment();
                    photoFragment.show(getSupportFragmentManager(), "");
                    photoFragment.setOnCameraPickListener(new PhotoFragment.onCameraPickListener() {
                        @Override
                        public void onOpenCamera() {
                            choseCamera();
                        }

                        @Override
                        public void onOpenImage() {
                            chosePhoto();
                        }
                    });

                } else {
                    // TODO: 2018/4/23 显示大图预览
                    Intent intent = new Intent(UploadAnswerActivity.this, AnswerPreviewActivity.class);
                    intent.putExtra("path", answerAdapter.getItem(position));
                    startActivity(intent);
                }

            }
        });
        answerAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                uploadInfos.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });

        RxView.clicks(tvNext).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (uploadInfos.size() == 0) {
                    ToastUtils.showCenterToast(UploadAnswerActivity.this, "请上传书本答案");
                    return;
                }
                Intent intent = new Intent(UploadAnswerActivity.this, PerfectBookInfoActivity.class);
                intent.putExtra("cover_img", coverImg);
                intent.putExtra("cover_id", bookId);
//                intent.putExtra("answer_list",uploadInfos.toArray(new String[uploadInfos.size()]));
                intent.putStringArrayListExtra("answer_list", (ArrayList<String>) uploadInfos);
                startActivity(intent);
            }
        });

    }

    private void uploadData(String imagePath) {

        if (!uploadInfos.contains(imagePath)) {
            uploadInfos.add(0, imagePath);
            answerAdapter.notifyItemInserted(0);
        }

    }

    private void choseCamera() {
        FunctionConfig config = new FunctionConfig.Builder()
                .setMutiSelectMaxSize(16)
                .build();
        //带配置
        GalleryFinal.openCamera(100, config, mOnHanlderResultCallback);
    }

    private void chosePhoto() {
        FunctionConfig config = new FunctionConfig.Builder()
                .setMutiSelectMaxSize(16)
                .build();
        GalleryFinal.openGalleryMuti(100, config, mOnHanlderResultCallback);
    }

    /**
     * 选择照片的监听事件
     */
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {


        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            Log.d(TAG, resultList.size() + "");//返回的相册列表
            if (resultList.size() > 0) {
                for (PhotoInfo photoInfo : resultList) {
                    mPresenter.getOssInfo(photoInfo.getPhotoPath(), "answer", bookId);
                }
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Log.d(TAG, "选择图片错误 状态码：" + requestCode + "errorMsg=" + errorMsg);//返回的相册列表
        }
    };

    @Override
    public void showUploadResult(BookInfo body) {
        uploadData(body.getAnswer_img());
    }

    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.FINISH)
            })
    public void finishSelf(String finish) {
        finish();
    }
}

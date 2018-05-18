package com.yc.answer.index.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.vondear.rxtools.RxSPTool;
import com.yc.answer.R;
import com.yc.answer.constant.SpConstant;
import com.yc.answer.index.contract.PerfectBookContract;
import com.yc.answer.index.model.bean.BookInfo;
import com.yc.answer.index.presenter.PerfectBookPresenter;
import com.yc.answer.setting.ui.widget.BaseSettingView;
import com.yc.answer.utils.ToastUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import rx.functions.Func1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/4/20 17:09.
 */

public class PerfectBookInfoActivity extends BaseActivity<PerfectBookPresenter> implements PerfectBookContract.View {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.common_tv_title)
    TextView commonTvTitle;
    @BindView(R.id.baseBookView)
    BaseSettingView baseBookView;
    @BindView(R.id.baseGradeView)
    BaseSettingView baseGradeView;
    @BindView(R.id.baseSubjectView)
    BaseSettingView baseSubjectView;
    @BindView(R.id.tv_done)
    TextView tvDone;

    public static final int NAME = 1;
    public static final int GRADE = 2;
    public static final int SUBJECT = 3;
    private String coverImg;
    private List<String> anserList;
    private String bookId;

    @Override
    public int getLayoutId() {

        return R.layout.activity_perfect_book_info;
    }

    @Override
    public void init() {
        commonTvTitle.setText(getString(R.string.book_info));

        mPresenter = new PerfectBookPresenter(this, this);
        coverImg = getIntent().getStringExtra("cover_img");
        bookId = getIntent().getStringExtra("cover_id");
        anserList = getIntent().getStringArrayListExtra("answer_list");
        initData();

        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        RxView.clicks(baseBookView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(PerfectBookInfoActivity.this, PerfectBookDetailInfoActivity.class);
                intent.putExtra("pos", 0);
                startActivityForResult(intent, NAME);
            }
        });
        RxView.clicks(baseGradeView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(PerfectBookInfoActivity.this, PerfectBookDetailInfoActivity.class);
                intent.putExtra("pos", 1);
                startActivityForResult(intent, GRADE);
            }
        });
        RxView.clicks(baseSubjectView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(PerfectBookInfoActivity.this, PerfectBookDetailInfoActivity.class);
                intent.putExtra("pos", 2);
                startActivityForResult(intent, SUBJECT);
            }
        });
        RxView.clicks(tvDone).filter(new Func1<Void, Boolean>() {
            @Override
            public Boolean call(Void aVoid) {
                if (TextUtils.isEmpty(baseBookView.getExtraText())) {
                    ToastUtils.showCenterToast(PerfectBookInfoActivity.this, "书本名称不能为空");
                } else if (TextUtils.isEmpty(baseGradeView.getExtraText())) {
                    ToastUtils.showCenterToast(PerfectBookInfoActivity.this, "年级不能为空");
                } else if (TextUtils.isEmpty(baseSubjectView.getExtraText())) {
                    ToastUtils.showCenterToast(PerfectBookInfoActivity.this, "科目不能为空");
                }

                return !TextUtils.isEmpty(baseBookView.getExtraText()) && !TextUtils.isEmpty(baseGradeView.getExtraText()) && !TextUtils.isEmpty(baseSubjectView.getExtraText());
            }
        }).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                // TODO: 2018/4/23 上传书籍相关信息
                BookInfo bookInfo = new BookInfo();
                bookInfo.setId(bookId);
                bookInfo.setCover_img(coverImg);
                bookInfo.setAnswer_list(anserList);
                bookInfo.setGrade(baseGradeView.getExtraText());
                bookInfo.setName(baseBookView.getExtraText());
                bookInfo.setSubject(baseSubjectView.getExtraText());
                mPresenter.perfectBook(bookInfo);
            }
        });

    }

    private void initData() {
        baseBookView.setExtraText(RxSPTool.getString(this, SpConstant.BOOK_NAME));
        String grades = RxSPTool.getString(this, SpConstant.BOOK_GRADE);
        if (!TextUtils.isEmpty(grades)) {
            baseGradeView.setExtraText(grades.split("-")[2]);
        }
        String subjects = RxSPTool.getString(this, SpConstant.BOOK_SUBJECT);
        if (!TextUtils.isEmpty(subjects)) {
            baseSubjectView.setExtraText(subjects.split("-")[0]);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String result = data.getStringExtra("result");
            switch (requestCode) {
                case NAME:
                    baseBookView.setExtraText(result);
                    break;
                case GRADE:
                    baseGradeView.setExtraText(result);
                    break;
                case SUBJECT:
                    baseSubjectView.setExtraText(result);
                    break;
            }
        }
    }
}

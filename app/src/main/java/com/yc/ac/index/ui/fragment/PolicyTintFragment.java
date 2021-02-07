package com.yc.ac.index.ui.fragment;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.vondear.rxtools.RxSPTool;
import com.yc.ac.R;
import com.yc.ac.base.BaseEngine;
import com.yc.ac.base.WebActivity;
import com.yc.ac.constant.SpConstant;
import com.yc.ac.utils.SpanUtils;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseDialogFragment;
import yc.com.base.BasePresenter;
import yc.com.base.IView;

/**
 * Created by suns  on 2021/1/28 14:02.
 */
public class PolicyTintFragment extends BaseDialogFragment<BasePresenter<BaseEngine, IView>> {


    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_agree_btn)
    TextView tvAgreeBtn;
    @BindView(R.id.tv_exit_btn)
    TextView tvExitBtn;

    @Override
    protected float getWidth() {
        return 0.75f;
    }

    @Override
    public int getAnimationId() {
        return 0;
    }

    @Override
    public int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_policy_tint;
    }

    @Override
    public void init() {
        setCancelable(false);
        String link = "为了加强对您个人信息的保护,根据最新法律法规要求,更新了隐松改策,请您仔细阅读并确认\"<a href=\"https://answer.bshu.com/v1/treaty\">隐私权相关政策</a>\"(特别是加粗或下划线标注的内容),我们严格按照政策内容使用和保扬心的个人信息,为您提供更好的服务,感谢您的信任。";
//        tvContent.setText(HtmlCompat.fromHtml("为了加强对您个人信息的保护,根据最新法律法规要求,更新了隐松改策,请您仔细阅读并确认\"<a href=\"https://answer.bshu.com/v1/treaty\">隐私权相关政策</a>\"(特别是加粗或下划线标注的内容),我们严格按照政策内容使用和保扬心的个人信息,为您提供更好的服务,感谢您的信任。", HtmlCompat.FROM_HTML_MODE_COMPACT));
        tvContent.setLinkTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());

        tvContent.setText(SpanUtils.getStringBuilder(getActivity(),link,getString(R.string.policy_title)));

        RxView.clicks(tvAgreeBtn).subscribe(aVoid -> {
            RxSPTool.putBoolean(getActivity(), SpConstant.INDEX_DIALOG, true);

            dismiss();
        });
        RxView.clicks(tvExitBtn).subscribe(aVoid -> {
            dismiss();
            android.os.Process.killProcess(android.os.Process.myPid());
        });
    }
}

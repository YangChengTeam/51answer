package com.yc.ac.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ParagraphStyle;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.yc.ac.R;
import com.yc.ac.base.WebActivity;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;

/**
 * Created by wanglin  on 2018/3/10 08:58.
 */

public class SpanUtils {

    // 设置hint字体大小
    public static void setEditTextHintSize(EditText editText) {

        CharSequence hint = editText.getHint();
        SpannableString ss = new SpannableString(hint);
        //16为字体大小,可以修改
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(16, true);
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        editText.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }


    public static SpannableStringBuilder getStringBuilder(Context context, String html, String title) {
        Spanned spanned = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT);
        if (spanned instanceof SpannableStringBuilder) {
            SpannableStringBuilder spannableStringBuilder = (SpannableStringBuilder) spanned;

            Object[] objs = spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), URLSpan.class);

            if (null != objs && objs.length != 0) {
                for (Object obj : objs) {
                    int start = spannableStringBuilder.getSpanStart(obj);
                    int end = spannableStringBuilder.getSpanEnd(obj);
                    if (obj instanceof URLSpan) {
                        //先移除这个Span，再新添加一个自己实现的Span。
                        URLSpan span = (URLSpan) obj;
                        final String url = span.getURL();
                        spannableStringBuilder.removeSpan(obj);

                        spannableStringBuilder.setSpan(new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View widget) {
                                WebActivity.startActivity(context, url, title);
                            }
                        }, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    }

                }
            }
            return spannableStringBuilder;
        }
        return new SpannableStringBuilder(html);

    }
}

package com.yc.answer.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.widget.EditText;

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
}

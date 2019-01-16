package com.yc.ac.index.ui.fragment;

import android.text.TextUtils;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.vondear.rxtools.RxSPTool;
import com.yc.ac.R;
import com.yc.ac.constant.SpConstant;
import com.yc.ac.index.ui.activity.PerfectBookDetailInfoActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import yc.com.base.BaseFragment;

/**
 * Created by wanglin  on 2018/4/20 17:44.
 */

public class BookNameFragment extends BaseFragment {
    @BindView(R.id.et_book_name)
    EditText etBookName;

    @Override
    public int getLayoutId() {
        return R.layout.frament_book_name;
    }

    @Override
    public void init() {
        String name = RxSPTool.getString(getActivity(), SpConstant.BOOK_NAME);
        if (!TextUtils.isEmpty(name)) {
            etBookName.setText(name);
            etBookName.setSelection(name.length());
        }
        RxTextView.textChanges(etBookName).debounce(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<CharSequence>() {
            @Override
            public void call(CharSequence charSequence) {
                ((PerfectBookDetailInfoActivity) getActivity()).transmitData(0, charSequence.toString().trim());
            }
        });
    }


}

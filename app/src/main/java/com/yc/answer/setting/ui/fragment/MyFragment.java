package com.yc.answer.setting.ui.fragment;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ToastUtil;
import com.vondear.rxtools.RxPhotoTool;
import com.vondear.rxtools.view.dialog.RxDialogEditSureCancel;
import com.yc.answer.R;
import com.yc.answer.base.WebActivity;
import com.yc.answer.constant.BusAction;
import com.yc.answer.setting.contract.MyContract;
import com.yc.answer.setting.model.bean.UserInfo;
import com.yc.answer.setting.presenter.MyPresenter;
import com.yc.answer.setting.ui.activity.BindPhoneActivity;
import com.yc.answer.setting.ui.activity.LoginGroupActivity;
import com.yc.answer.setting.ui.activity.SettingActivity;
import com.yc.answer.setting.ui.activity.StatementActivity;
import com.yc.answer.setting.ui.widget.BaseSettingView;
import com.yc.answer.setting.ui.widget.FollowWeiXinPopupWindow;
import com.yc.answer.utils.ActivityUtils;
import com.yc.answer.utils.QQUtils;
import com.yc.answer.utils.ShareInfoHelper;
import com.yc.answer.utils.UserInfoHelper;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.BaseFragment;


/**
 * Created by wanglin  on 2018/3/7 13:53.
 */

public class MyFragment extends BaseFragment<MyPresenter> implements MyContract.View {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.ll_head)
    LinearLayout llHead;
    @BindView(R.id.toolbarWarpper)
    FrameLayout toolbarWarpper;
    @BindView(R.id.baseSettingView_wx)
    BaseSettingView baseSettingViewWx;
    @BindView(R.id.baseSettingView_share)
    BaseSettingView baseSettingViewShare;
    @BindView(R.id.baseSettingView_net)
    BaseSettingView baseSettingViewNet;
    @BindView(R.id.baseSettingView_setting)
    BaseSettingView baseSettingViewSetting;
    @BindView(R.id.ll_primary_school)
    LinearLayout llPrimarySchool;
    @BindView(R.id.ll_middle_school)
    LinearLayout llMiddleSchool;
    @BindView(R.id.tv_statement)
    TextView tvStatement;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    public void init() {
        mPresenter = new MyPresenter(getActivity(), this);
        initListener();


    }

    private void initListener() {
        RxView.clicks(baseSettingViewWx).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), LoginGroupActivity.class));
            }
        });

        RxView.clicks(baseSettingViewSetting).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });

        RxView.clicks(tvNickname).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!UserInfoHelper.isGoToLogin(getActivity())) {//设置昵称
                    showDioloag();
                }
            }
        });
        RxView.clicks(ivAvatar).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!UserInfoHelper.isGoToLogin(getActivity())) {//更改图像
                    RxPhotoTool.openLocalImage(getActivity());
                }
            }
        });
        RxView.clicks(tvPhone).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!UserInfoHelper.isGoToLogin(getActivity())) {//更改手机号
                    Intent intent = new Intent(getActivity(), BindPhoneActivity.class);
                    intent.putExtra("flag", "修改");
                    startActivity(intent);
                }
            }
        });

        RxView.clicks(baseSettingViewWx).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                FollowWeiXinPopupWindow followWeiXinPopupWindow = new FollowWeiXinPopupWindow(getActivity());
                followWeiXinPopupWindow.show();
            }
        });

        RxView.clicks(llPrimarySchool).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                QQUtils.joinQQZhongXueGroup(getActivity(), "VPvV6KlVsB5sROLTwoQlk-eD6MZSAXYw");
            }
        });

        RxView.clicks(llMiddleSchool).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                QQUtils.joinQQZhongXueGroup(getActivity(), "Ik1JY_oz-loc2r9OxDsVcUobxD-PmS9K");
            }
        });

        RxView.clicks(baseSettingViewShare).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ShareFragment shareFragment = new ShareFragment();
                shareFragment.setShareInfo(ShareInfoHelper.getShareInfo());
                shareFragment.show(getFragmentManager(), null);
            }
        });

        RxView.clicks(baseSettingViewNet).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", "http://m.upkao.com/zk/");
                startActivity(intent);
            }
        });

        RxView.clicks(tvStatement).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), StatementActivity.class));
            }
        });

    }

    private void showDioloag() {
        final RxDialogEditSureCancel rxDialogEditSureCancel = new RxDialogEditSureCancel(getActivity());//提示弹窗
        rxDialogEditSureCancel.getTitleView().setText("请输入你的昵称");
        final EditText editText = rxDialogEditSureCancel.getEditText();
//                    editText.setHint(tvNickname.getText());
        rxDialogEditSureCancel.getSureView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String text = editText.getText().toString().trim();
                if (TextUtils.isEmpty(text)) {
                    ToastUtil.toast(getActivity(), "昵称不能为空");
                    return;
                }
                mPresenter.updateInfo(text, "", "");
                rxDialogEditSureCancel.cancel();
            }
        });
        rxDialogEditSureCancel.getCancelView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogEditSureCancel.cancel();
            }
        });
        rxDialogEditSureCancel.show();
    }


    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.LOGIN_OUT)
            })
    @Override
    public void showNotLogin(String b) {
        if (ActivityUtils.isValidContext(getActivity())) {
            ivAvatar.setImageResource(R.mipmap.default_avatar);
            tvPhone.setVisibility(View.GONE);
            tvNickname.setText(getString(R.string.not_login));
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.LOGIN_SUCCESS)
            })

    @Override
    public void showUserInfo(UserInfo userInfo) {
        Glide.with(getActivity()).load(userInfo.getFace()).apply(new RequestOptions().error(R.mipmap.default_avatar).circleCrop()).into(ivAvatar);
        String nick_name = userInfo.getNick_name();
        if (TextUtils.isEmpty(nick_name)) {
            nick_name = "还没设置昵称,快来设置吧!";
        }
        tvNickname.setText(nick_name);

        if (TextUtils.isEmpty(userInfo.getMobile())) {
            tvPhone.setVisibility(View.GONE);
        } else {
            tvPhone.setText(userInfo.getMobile());
            tvPhone.setVisibility(View.VISIBLE);
        }


    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.GET_PICTURE)
            }
    )
    public void getPicture(Uri uri) {
        String path = RxPhotoTool.getImageAbsolutePath(getActivity(), uri);
        File file = new File(path);
        mPresenter.uploadFile(file, path.substring(path.lastIndexOf("/") + 1));
    }

    @Override
    public void showLoadingDialog(String mess) {
        ((BaseActivity) getActivity()).showLoadingDialog(mess);
    }

    @Override
    public void dismissDialog() {
        ((BaseActivity) getActivity()).dismissDialog();
    }

}

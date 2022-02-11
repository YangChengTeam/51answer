package com.yc.ac.setting.ui.fragment;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ToastUtil;
import com.vondear.rxtools.RxPhotoTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.dialog.RxDialogEditSureCancel;
import com.yc.ac.R;
import com.yc.ac.base.WebActivity;
import com.yc.ac.constant.BusAction;
import com.yc.ac.constant.SpConstant;
import com.yc.ac.index.ui.activity.PayActivity;
import com.yc.ac.setting.contract.MyContract;
import com.yc.ac.setting.model.bean.QbInfo;
import com.yc.ac.setting.model.bean.ShareInfo;
import com.yc.ac.setting.model.bean.TaskListInfo;
import com.yc.ac.setting.model.bean.UserInfo;
import com.yc.ac.setting.presenter.MyPresenter;
import com.yc.ac.setting.ui.activity.BindPhoneActivity;
import com.yc.ac.setting.ui.activity.BrowserActivity;
import com.yc.ac.setting.ui.activity.NotificationActivity;
import com.yc.ac.setting.ui.activity.PrivacyStatementActivity;
import com.yc.ac.setting.ui.activity.RechargeRecordActivity;
import com.yc.ac.setting.ui.activity.SettingActivity;
import com.yc.ac.setting.ui.activity.StatementActivity;
import com.yc.ac.setting.ui.activity.UserPolicyActivity;
import com.yc.ac.setting.ui.widget.BaseSettingView;
import com.yc.ac.utils.ActivityUtils;
import com.yc.ac.utils.QQUtils;
import com.yc.ac.utils.ShareInfoHelper;
import com.yc.ac.utils.ToastUtils;
import com.yc.ac.utils.UserInfoHelper;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import yc.com.base.BaseActivity;
import yc.com.base.BaseFragment;

import static com.umeng.socialize.utils.ContextUtil.getPackageName;


/**
 * Created by wanglin  on 2018/3/7 13:53.
 */

public class MyFragment extends BaseFragment<MyPresenter> implements MyContract.View {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_login_register)
    TextView tvLoginRegister;
    @BindView(R.id.ll_not_login)
    LinearLayout llNotLogin;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;
    @BindView(R.id.ll_head)
    LinearLayout llHead;
    @BindView(R.id.toolbarWarpper)
    FrameLayout toolbarWarpper;
    @BindView(R.id.ll_primary_school)
    LinearLayout llPrimarySchool;
    @BindView(R.id.ll_middle_school)
    LinearLayout llMiddleSchool;

    @BindView(R.id.baseSettingView_service)
    BaseSettingView baseSettingViewService;
    @BindView(R.id.baseSettingView_setting)
    BaseSettingView baseSettingViewSetting;
    @BindView(R.id.tv_statement)
    TextView tvStatement;


    @BindView(R.id.baseSettingView_browser)
    BaseSettingView baseSettingViewBrowser;
    @BindView(R.id.baseSettingView_privacy_statement)
    BaseSettingView baseSettingViewPrivacyStatement;
    @BindView(R.id.baseSettingView_user_policy)
    BaseSettingView baseSettingViewUserPolicy;
    @BindView(R.id.baseSettingView_recharge)
    BaseSettingView baseSettingViewRecharge;
    @BindView(R.id.baseSettingView_notification)
    BaseSettingView baseSettingViewNotification;


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


        RxView.clicks(baseSettingViewSetting).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            if (!UserInfoHelper.isGoToLogin(getActivity()))
                startActivity(new Intent(getActivity(), SettingActivity.class));
        });

        RxView.clicks(tvNickname).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            if (!UserInfoHelper.isGoToLogin(getActivity())) {//设置昵称
                showDialog();
            }
        });
        RxView.clicks(ivAvatar).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            if (!UserInfoHelper.isGoToLogin(getActivity())) {//更改图像
                RxPhotoTool.openLocalImage(requireActivity());
            }
        });
        RxView.clicks(tvPhone).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            if (!UserInfoHelper.isGoToLogin(getActivity())) {//更改手机号
                Intent intent = new Intent(getActivity(), BindPhoneActivity.class);
                intent.putExtra("flag", "修改");
                startActivity(intent);
            }
        });
        RxView.clicks(tvLoginRegister).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            if (!UserInfoHelper.isGoToLogin(getActivity())) {
                //登录成功
            }
        });


        RxView.clicks(llPrimarySchool).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> QQUtils.joinQQZhongXueGroup(getActivity(), "VPvV6KlVsB5sROLTwoQlk-eD6MZSAXYw"));

        RxView.clicks(llMiddleSchool).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> QQUtils.joinQQZhongXueGroup(getActivity(), "Ik1JY_oz-loc2r9OxDsVcUobxD-PmS9K"));


        RxView.clicks(tvStatement).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> startActivity(new Intent(getActivity(), StatementActivity.class)));


        RxView.clicks(baseSettingViewService).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> joinQQ("2645034912"));


        RxView.clicks(baseSettingViewBrowser).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> startActivity(new Intent(getActivity(), BrowserActivity.class)));
        RxView.clicks(baseSettingViewPrivacyStatement).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid ->
                        WebActivity.startActivity(getActivity(), "http://answer.bshu.com/html/daanquan.html", getString(R.string.policy_title))
//                startActivity(new Intent(getActivity(), PrivacyStatementActivity.class))
        );
        RxView.clicks(baseSettingViewUserPolicy).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> startActivity(new Intent(getActivity(), UserPolicyActivity.class)));

        RxView.clicks(baseSettingViewRecharge).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> startActivity(new Intent(getActivity(), RechargeRecordActivity.class)));
        RxView.clicks(baseSettingViewNotification).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> startActivity(new Intent(getActivity(), NotificationActivity.class)));
    }


    /**
     * 跳转QQ聊天界面
     */
    public void joinQQ(String qqNum) {
        try {
            //第二种方式：可以跳转到添加好友，如果qq号是好友了，直接聊天
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qqNum;//uin是发送过去的qq号码
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.toast2(getActivity(), "请检查是否安装QQ");
        }
    }


    private void showDialog() {
        final RxDialogEditSureCancel rxDialogEditSureCancel = new RxDialogEditSureCancel(getActivity());//提示弹窗
        rxDialogEditSureCancel.getTitleView().setText("请输入你的昵称");
        final EditText editText = rxDialogEditSureCancel.getEditText();
//                    editText.setHint(tvNickname.getText());
        rxDialogEditSureCancel.getSureView().setOnClickListener(v -> {
            String text = editText.getText().toString().trim();
            if (TextUtils.isEmpty(text)) {
                ToastUtil.toast(getActivity(), "昵称不能为空");
                return;
            }
            mPresenter.updateInfo(text, "", "");
            rxDialogEditSureCancel.cancel();
        });
        rxDialogEditSureCancel.getCancelView().setOnClickListener(v -> rxDialogEditSureCancel.cancel());
        rxDialogEditSureCancel.show();
    }


    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.LOGIN_OUT)
            })
    @Override
    public void showNotLogin(String b) {
        if (ActivityUtils.isValidContext(getActivity())) {
            ivAvatar.setImageResource(R.mipmap.default_not_login);
            llNotLogin.setVisibility(View.VISIBLE);
            llLogin.setVisibility(View.GONE);

        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.LOGIN_SUCCESS)
            })

    @Override
    public void showUserInfo(UserInfo userInfo) {
        llLogin.setVisibility(View.VISIBLE);
        llNotLogin.setVisibility(View.GONE);
        Glide.with(requireActivity()).load(userInfo.getFace()).apply(new RequestOptions().error(R.mipmap.default_login).circleCrop()).into(ivAvatar);
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


    @Override
    public void hide() {
    }

    @Override
    public void showNoData() {

    }

    @Override
    public void showNoNet() {

    }


}

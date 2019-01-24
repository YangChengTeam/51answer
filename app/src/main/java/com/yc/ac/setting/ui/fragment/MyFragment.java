package com.yc.ac.setting.ui.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.kk.utils.ToastUtil;
import com.vondear.rxtools.RxPhotoTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.dialog.RxDialogEditSureCancel;
import com.yc.ac.R;
import com.yc.ac.base.StateView;
import com.yc.ac.base.WebActivity;
import com.yc.ac.constant.BusAction;
import com.yc.ac.constant.SpConstant;
import com.yc.ac.index.ui.widget.MyDecoration;
import com.yc.ac.setting.contract.MyContract;
import com.yc.ac.setting.model.bean.QbInfo;
import com.yc.ac.setting.model.bean.TaskListInfo;
import com.yc.ac.setting.model.bean.UserInfo;
import com.yc.ac.setting.presenter.MyPresenter;
import com.yc.ac.setting.ui.activity.BindPhoneActivity;
import com.yc.ac.setting.ui.activity.InvitationActivity;
import com.yc.ac.setting.ui.activity.InvitationFriendActicity;
import com.yc.ac.setting.ui.activity.LoginGroupActivity;
import com.yc.ac.setting.ui.activity.SettingActivity;
import com.yc.ac.setting.ui.activity.StatementActivity;
import com.yc.ac.setting.ui.activity.UploadBookIntroduceActivity;
import com.yc.ac.setting.ui.adapter.TaskListAdapter;
import com.yc.ac.setting.ui.widget.BaseIncomeView;
import com.yc.ac.setting.ui.widget.BaseSettingView;
import com.yc.ac.setting.ui.widget.FollowWeiXinPopupWindow;
import com.yc.ac.utils.ActivityUtils;
import com.yc.ac.utils.QQUtils;
import com.yc.ac.utils.ShareInfoHelper;
import com.yc.ac.utils.ToastUtils;
import com.yc.ac.utils.UserInfoHelper;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.functions.Action1;
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
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.ll_head)
    LinearLayout llHead;
    @BindView(R.id.toolbarWarpper)
    FrameLayout toolbarWarpper;


    @BindView(R.id.baseSettingView_setting)
    BaseSettingView baseSettingViewSetting;
    @BindView(R.id.ll_primary_school)
    LinearLayout llPrimarySchool;
    @BindView(R.id.ll_middle_school)
    LinearLayout llMiddleSchool;
    @BindView(R.id.tv_statement)
    TextView tvStatement;
    @BindView(R.id.tv_login_register)
    TextView tvLoginRegister;
    @BindView(R.id.ll_not_login)
    LinearLayout llNotLogin;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;

    @BindView(R.id.tv_detail)
    TextView tvDetail;
    @BindView(R.id.tv_qb)
    TextView tvQb;
    @BindView(R.id.earning_recyclerView)
    RecyclerView earningRecyclerView;
    @BindView(R.id.stateView)
    StateView stateView;


    private TaskListAdapter taskListAdapter;

    private long startTime;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    public void init() {
        mPresenter = new MyPresenter(getActivity(), this);

        earningRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskListAdapter = new TaskListAdapter(null);

        earningRecyclerView.setAdapter(taskListAdapter);

        earningRecyclerView.addItemDecoration(new MyDecoration(10));

        initListener();

    }

    private void initListener() {


        RxView.clicks(baseSettingViewSetting).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!UserInfoHelper.isGoToLogin(getActivity()))
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
        RxView.clicks(tvLoginRegister).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!UserInfoHelper.isGoToLogin(getActivity())) {
                    //登录成功
                }
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


        RxView.clicks(tvStatement).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), StatementActivity.class));
            }
        });


        RxView.clicks(tvDetail).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
//                startActivity(new Intent(getActivity(), EarningsDetailActivity.class));
                //申请提现
                ApplyDepositFragment applyDepositFragment = new ApplyDepositFragment();
                applyDepositFragment.show(getFragmentManager(), "");
            }
        });

        taskListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TaskListInfo taskListInfo = taskListAdapter.getItem(position);
                if (taskListInfo != null && !TextUtils.isEmpty(taskListInfo.getName())) {
                    if (taskListInfo.getName().contains("邀请")) {
                        startActivity(new Intent(getActivity(), InvitationFriendActicity.class));
                    } else if (taskListInfo.getName().contains("好评")) {
                        startTime = System.currentTimeMillis();
                        //好评赚钱
                        try {
                            Uri uri = Uri.parse("market://details?id=" + getPackageName());
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showCenterToast(getActivity(), "你手机安装的应用市场没有上线该应用，请前往其他应用市场进行点评");
                        }
                    } else if (taskListInfo.getName().contains("分享")) {
                        ShareFragment shareFragment = new ShareFragment();
                        shareFragment.setIsShareMoney(true);
                        shareFragment.setShareInfo(ShareInfoHelper.getShareInfo());
                        shareFragment.show(getActivity().getSupportFragmentManager(), "");
                    } else if (taskListInfo.getName().contains("上传")) {
                        startActivity(new Intent(getActivity(), UploadBookIntroduceActivity.class));
                    }
                }
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
            ivAvatar.setImageResource(R.mipmap.default_not_login);
            llNotLogin.setVisibility(View.VISIBLE);
            llLogin.setVisibility(View.GONE);
            tvQb.setText(String.valueOf("0.00"));

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
        Glide.with(getActivity()).load(userInfo.getFace()).apply(new RequestOptions().error(R.mipmap.default_login).circleCrop()).into(ivAvatar);
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
        mPresenter.getQbInfo();


    }

    @Override
    public void showQbInfo(QbInfo info) {
        BigDecimal bd = new BigDecimal(info.qb);

        tvQb.setText(String.valueOf(bd.setScale(2)));
    }

    @Override
    public void showTaskList(List<TaskListInfo> list) {
        LogUtil.msg("desp: list " + list.size());
        taskListAdapter.setNewData(list);
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

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.SHARE_MONEY_SUCCESS)
            }
    )
    public void shareMoneySuccess(String result) {
        mPresenter.getQbInfo();
        mPresenter.getTaskInfoList(true);
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
    public void onResume() {
        super.onResume();
        if (startTime > 0 && !RxSPTool.getBoolean(getActivity(), SpConstant.OPEN_MARKET)) {
            if ((System.currentTimeMillis() - startTime) / 1000 >= 5) {
                //跳到应用市场
                mPresenter.comment(UserInfoHelper.getUId());
            }
        }
    }


    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showNoData() {
        stateView.showNoData(earningRecyclerView);
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(earningRecyclerView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getTaskInfoList(true);
            }
        });
    }
}

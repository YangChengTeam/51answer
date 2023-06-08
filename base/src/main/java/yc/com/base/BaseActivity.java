package yc.com.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;

import com.umeng.analytics.MobclickAgent;
import com.vondear.rxtools.RxLogTool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import primary.answer.yc.com.base.R;

/**
 * Created by wanglin  on 2018/3/6 10:14.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements IView, IDialog {


    protected P mPresenter;
    protected BaseLoadingView baseLoadingView;
    protected Handler mHandler;
    private MyRunnable taskRunnable;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        RxBus.get().register(this);
        try {
            ButterKnife.bind(this);
        } catch (Exception e) {
            RxLogTool.e("-->: 初始化失败 " + e.getMessage());
        }

        baseLoadingView = new BaseLoadingView(this);
        mHandler = new Handler();
        //顶部透明

        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        init();
        setStatusBar();
    }

    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, null);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (EmptyUtils.isNotEmpty(mPresenter)) {
            Log.d("ccc", "-----00-----onResume: ");
            MobclickAgent.onResume(this);
            mPresenter.subscribe();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (EmptyUtils.isNotEmpty(mPresenter)) {
            Log.d("ccc", "------11----onPause: ");
            MobclickAgent.onPause(this);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (EmptyUtils.isNotEmpty(mPresenter)) {
            mPresenter.unsubscribe();
        }
        mHandler.removeCallbacks(taskRunnable);
        taskRunnable = null;
        mHandler = null;
        RxBus.get().unregister(this);
    }

    @Override
    public void showLoadingDialog(String mess) {
        if (!this.isFinishing()) {
            if (null != baseLoadingView) {
                baseLoadingView.setMessage(mess);
                baseLoadingView.show();
            }
        }
    }

    @Override
    public void dismissDialog() {
        try {
            if (!this.isFinishing()) {
                if (null != baseLoadingView && baseLoadingView.isShowing()) {
                    baseLoadingView.dismiss();
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * 改变获取验证码按钮状态
     */
    public void showGetCodeDisplay(TextView textView) {
        taskRunnable = new MyRunnable(textView);
        if (null != mHandler) {
            mHandler.removeCallbacks(taskRunnable);
            mHandler.removeMessages(0);
            totalTime = 60;
            textView.setClickable(false);
//            textView.setTextColor(ContextCompat.getColor(R.color.coment_color));
//            textView.setBackgroundResource(R.drawable.bg_btn_get_code);
            if (null != mHandler) mHandler.postDelayed(taskRunnable, 0);
        }
    }

    /**
     * 定时任务，模拟倒计时广告
     */
    private int totalTime = 60;



    private class MyRunnable implements Runnable {
        TextView mTv;

        public MyRunnable(TextView textView) {
            this.mTv = textView;
        }

        @Override
        public void run() {
            mTv.setText(totalTime + "秒后重试");
            totalTime--;
            if (totalTime < 0) {
                //还原
                initGetCodeBtn(mTv);
                return;
            }
            if (null != mHandler) mHandler.postDelayed(this, 1000);
        }
    }


    /**
     * 还原获取验证码按钮状态
     */
    private void initGetCodeBtn(TextView textView) {
        totalTime = 0;
        if (null != taskRunnable && null != mHandler) {
            mHandler.removeCallbacks(taskRunnable);
            mHandler.removeMessages(0);
        }
        textView.setText("重新获取");
        textView.setClickable(true);
//        textView.setTextColor(CommonUtils.getColor(R.color.white));
//        textView.setBackgroundResource(R.drawable.bg_btn_get_code_true);
    }

    private boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}

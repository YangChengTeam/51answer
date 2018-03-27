package yc.com.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwangjr.rxbus.RxBus;
import com.umeng.analytics.MobclickAgent;
import com.vondear.rxtools.RxLogTool;

import butterknife.ButterKnife;

/**
 * Created by wanglin  on 2018/3/6 10:52.
 */

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements IView {


    private View rootView;
    protected P mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        RxBus.get().register(this);
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);

        }
        try {
            ButterKnife.bind(this, rootView);
        } catch (Exception e) {
            RxLogTool.e("-->:初始化失败 :" + e.getMessage());
        }


        init();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (EmptyUtils.isNotEmpty(mPresenter)) {
            mPresenter.subscribe();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (EmptyUtils.isNotEmpty(mPresenter)) {
            mPresenter.unsubscribe();
        }
        RxBus.get().unregister(this);
        Runtime.getRuntime().gc();
    }

}

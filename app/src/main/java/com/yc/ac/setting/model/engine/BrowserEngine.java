package com.yc.ac.setting.model.engine;

import android.content.Context;

import com.yc.ac.base.BaseEngine;
import com.yc.ac.base.MyApp;
import com.yc.ac.setting.model.bean.BrowserInfo;
import com.yc.ac.setting.model.bean.BrowserInfoDao;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wanglin  on 2019/3/15 10:36.
 */
public class BrowserEngine extends BaseEngine {
    private BrowserInfoDao browserInfoDao;

    public BrowserEngine(Context context) {
        super(context);
        browserInfoDao = MyApp.getDaoSession().getBrowserInfoDao();
    }

    public Observable<List<BrowserInfo>> getBrowserInfos(final int page, final int pageSize) {
        return Observable.just("").subscribeOn(Schedulers.io()).map(new Func1<String, List<BrowserInfo>>() {
            @Override
            public List<BrowserInfo> call(String s) {
                return browserInfoDao.queryBuilder().orderDesc(BrowserInfoDao.Properties.SaveTime).limit(pageSize).offset((page - 1) * pageSize).list();
            }
        }).observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<Void> deleteBrowserInfos(final BrowserInfo browserInfo) {
        return Observable.just("").subscribeOn(Schedulers.io()).map(new Func1<String, Void>() {
            @Override
            public Void call(String s) {
                browserInfoDao.delete(browserInfo);
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread());


    }

}

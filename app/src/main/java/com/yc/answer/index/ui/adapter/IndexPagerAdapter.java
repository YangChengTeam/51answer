package com.yc.answer.index.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yc.answer.index.model.bean.VersionDetailInfo;

import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;

/**
 * Created by wanglin  on 2018/3/7 18:41.
 */

public class IndexPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragmentList;
    private List<VersionDetailInfo> mTitles;

    public IndexPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<VersionDetailInfo> titles) {

        super(fm);
        this.mFragmentList = fragments;
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position).getName();
    }
}

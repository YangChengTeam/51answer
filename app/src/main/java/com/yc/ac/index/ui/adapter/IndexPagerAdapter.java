package com.yc.ac.index.ui.adapter;

import com.yc.ac.index.model.bean.VersionDetailInfo;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

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

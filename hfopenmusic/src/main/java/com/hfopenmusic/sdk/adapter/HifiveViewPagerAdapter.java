package com.hfopenmusic.sdk.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * viewpager适配器
 * @author huchao
 */
public class HifiveViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList;//界面
    private final FragmentManager fm;
    public HifiveViewPagerAdapter(FragmentManager mFragmentManager, List<Fragment> fragmentList) {
        super (mFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        fm = mFragmentManager;
        mFragmentList = fragmentList;
    }

    @Override
    public long getItemId(int position) {
        //注意一定要重写
        return mFragmentList.get(position).hashCode();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (position < mFragmentList.size()) {
            fragment = mFragmentList.get(position);
        } else {
            fragment = mFragmentList.get(0);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mFragmentList == null ? 0 : mFragmentList.size();
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fm.beginTransaction().show(fragment).commitAllowingStateLoss();
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Fragment fragment = mFragmentList.get(position);
        fm.beginTransaction().hide(fragment).commitAllowingStateLoss();
    }

}

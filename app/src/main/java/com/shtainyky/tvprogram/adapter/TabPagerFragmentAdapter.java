package com.shtainyky.tvprogram.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.shtainyky.tvprogram.fragments.TVProgramViewPagerFragment;

import java.util.List;

public class TabPagerFragmentAdapter extends FragmentStatePagerAdapter {
    private List<String> mChannels;

    public TabPagerFragmentAdapter(FragmentManager fm, List<String> channels) {
        super(fm);
        mChannels = channels;

    }

    @Override
    public Fragment getItem(int position) {
        return TVProgramViewPagerFragment.newInstance(position+1);
    }

    @Override
    public int getCount() {
        return mChannels.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mChannels.get(position);
    }

}

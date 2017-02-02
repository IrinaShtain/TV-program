package com.shtainyky.tvprogram.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.shtainyky.tvprogram.fragments.TVProgramViewPagerFragment;

import java.util.List;

import static com.shtainyky.tvprogram.fragments.TVProgramViewPagerFragment.ARG_POSITION;

public class TabPagerFragmentAdapter extends FragmentStatePagerAdapter {
    private List<String> mChannels;

    public TabPagerFragmentAdapter(FragmentManager fm, List<String> channels) {
        super(fm);
        mChannels = channels;

    }

    @Override
    public Fragment getItem(int position) {
        TVProgramViewPagerFragment fragment = new TVProgramViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_POSITION, position + 1);
        fragment.setArguments(bundle);
        return fragment;
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

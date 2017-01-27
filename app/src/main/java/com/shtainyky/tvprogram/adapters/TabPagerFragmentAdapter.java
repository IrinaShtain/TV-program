package com.shtainyky.tvprogram.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shtainyky.tvprogram.navigationdrawerfragments.TVProgramViewPagerFragment;

public class TabPagerFragmentAdapter extends FragmentPagerAdapter {
    private String[] channels;
    private Context mContext;
    public TabPagerFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        channels = new String[]{"1", "25555555555555555555", "3", "5", " 6", "7", "8", "8", "8", "8"};
    }

    @Override
    public Fragment getItem(int position) {
        TVProgramViewPagerFragment fragment= new TVProgramViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return channels.length;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return channels[position];
    }

}

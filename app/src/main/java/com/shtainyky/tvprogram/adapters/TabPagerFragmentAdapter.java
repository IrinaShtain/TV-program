package com.shtainyky.tvprogram.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.navigationdrawerfragments.TVProgramViewPagerFragment;

import java.util.List;

public class TabPagerFragmentAdapter extends FragmentPagerAdapter {
    private List<String> mChannels;

    public TabPagerFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        DatabaseSource mSource = new DatabaseSource(context);
        mChannels = mSource.getAllChannelsTitles();

    }

    @Override
    public Fragment getItem(int position) {
        TVProgramViewPagerFragment fragment= new TVProgramViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position + 1);
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

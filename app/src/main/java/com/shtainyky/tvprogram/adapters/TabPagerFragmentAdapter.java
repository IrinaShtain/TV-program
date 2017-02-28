package com.shtainyky.tvprogram.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.shtainyky.tvprogram.fragments.TVProgramViewPagerFragment;
import com.shtainyky.tvprogram.models.models_ui.ChannelUI;

import java.util.List;

public class TabPagerFragmentAdapter extends FragmentStatePagerAdapter {
    private List<ChannelUI> mChannelItems;

    public TabPagerFragmentAdapter(FragmentManager fm, List<ChannelUI> channelItems) {
        super(fm);
        mChannelItems = channelItems;
    }

    @Override
    public Fragment getItem(int position) {
        int channelId = mChannelItems.get(position).getId();
        return TVProgramViewPagerFragment.newInstance(channelId);
    }

    @Override
    public int getCount() {
        return (mChannelItems != null) ? mChannelItems.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mChannelItems.get(position).getName();
    }

    public void swapListOfChannels(List<ChannelUI> channelItems) {
        mChannelItems = channelItems;
        notifyDataSetChanged();
    }

}

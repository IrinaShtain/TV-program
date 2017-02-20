package com.shtainyky.tvprogram.list_of_programs_displaying.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.list_of_programs_displaying.TVProgramViewPagerFragment;
import com.shtainyky.tvprogram.model.ChannelItem;

import java.util.List;

public class TabPagerFragmentAdapter extends FragmentStatePagerAdapter {
    private List<ChannelItem> channelItemList;

    public TabPagerFragmentAdapter(Context context, FragmentManager fm, boolean isPreferred) {
        super(fm);
        DatabaseSource mSource = new DatabaseSource(context);
        if (isPreferred) {
            channelItemList = mSource.getPreferredChannels();
        } else {
            channelItemList = mSource.getAllChannel();
        }

    }

    @Override
    public Fragment getItem(int position) {
        int channelId = channelItemList.get(position).getId();
        return TVProgramViewPagerFragment.newInstance(channelId);
    }

    @Override
    public int getCount() {
        return channelItemList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return channelItemList.get(position).getName();
    }

}

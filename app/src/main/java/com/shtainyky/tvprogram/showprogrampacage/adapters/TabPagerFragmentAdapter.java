package com.shtainyky.tvprogram.showprogrampacage.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.showprogrampacage.TVProgramViewPagerFragment;
import com.shtainyky.tvprogram.model.ChannelItem;

import java.util.List;

public class TabPagerFragmentAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private List<ChannelItem> channelItemList;
    private DatabaseSource mSource;

    public TabPagerFragmentAdapter(Context context, FragmentManager fm, boolean isPreferred) {
        super(fm);
        mContext = context;
        mSource = new DatabaseSource(mContext);
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

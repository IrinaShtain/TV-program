package com.shtainyky.tvprogram.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.shtainyky.tvprogram.fragments.TVProgramViewPagerFragment;
import com.shtainyky.tvprogram.model.ChannelItem;

import java.util.List;

// основна робота адаптера в тому щоб відобразити дані які в нього прийшли і "дати знати" про то що
// користувач взаємодіє з цими даними. Сам адаптер не вирішує нічого, не завантажує нічого,
// не обробляє нічого. Виконує тільки свої дії, дії АДАПТЕРА
public class TabPagerFragmentAdapter extends FragmentStatePagerAdapter {
    private List<ChannelItem> mChannelItems;

    public TabPagerFragmentAdapter(FragmentManager fm, List<ChannelItem> channelItems) {
        super(fm);
        mChannelItems = channelItems;
        // TODO: 20.02.17 do not get any data here, send channelItem list as input parameter
        //Done
    }

    @Override
    public Fragment getItem(int position) {
        int channelId = mChannelItems.get(position).getId();
        return TVProgramViewPagerFragment.newInstance(channelId);
    }

    @Override
    public int getCount() {
        return mChannelItems.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mChannelItems.get(position).getName();
    }

}

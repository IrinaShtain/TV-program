package com.shtainyky.tvprogram.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.shtainyky.tvprogram.fragments.TVProgramViewPagerFragment;
import com.shtainyky.tvprogram.model.ChannelItem;
import com.shtainyky.tvprogram.models.models_ui.CategoryUI;
import com.shtainyky.tvprogram.models.models_ui.ChannelUI;

import java.util.List;

// основна робота адаптера в тому щоб відобразити дані які в нього прийшли і "дати знати" про то що
// користувач взаємодіє з цими даними. Сам адаптер не вирішує нічого, не завантажує нічого,
// не обробляє нічого. Виконує тільки свої дії, дії АДАПТЕРА
public class TabPagerFragmentAdapter extends FragmentStatePagerAdapter {
    private List<ChannelUI> mChannelItems;

    public TabPagerFragmentAdapter(FragmentManager fm, List<ChannelUI> channelItems) {
        super(fm);
        mChannelItems = channelItems;
        // 20.02.17 do not get any data here, send channelItem list as input parameter
        //Done
    }

    @Override
    public Fragment getItem(int position) {
        int channelId = mChannelItems.get(position).getId();
        return TVProgramViewPagerFragment.newInstance(channelId);
    }

    @Override
    public int getCount() {
        if (mChannelItems != null)
            return mChannelItems.size();
        return 0;
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

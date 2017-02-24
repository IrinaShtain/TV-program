package com.shtainyky.tvprogram.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.adapters.TabPagerFragmentAdapter;
import com.shtainyky.tvprogram.database.DatabaseStoreImp;
import com.shtainyky.tvprogram.model.ChannelItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TVProgramFragment extends Fragment {
    public static final String ARG_PREFERRED = "is_preferred";
    private boolean mIsPreferred;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;


    public static TVProgramFragment newInstance(boolean preferredFlag) {
        TVProgramFragment fragment = new TVProgramFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_PREFERRED, preferredFlag);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tvprograms, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mIsPreferred = bundle.getBoolean(ARG_PREFERRED);
        }
        setupTabs();
        return view;
    }

    private void setupTabs() {
        TabPagerFragmentAdapter adapter = new TabPagerFragmentAdapter(getActivity().getSupportFragmentManager(),
                getChannelItems());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private List<ChannelItem> getChannelItems() {
        List<ChannelItem> channelItems;
        DatabaseStoreImp source = new DatabaseStoreImp(getContext());
        if (mIsPreferred) {
            channelItems = source.getPreferredChannels();
        } else {
            channelItems = source.getAllChannel();
        }
        return channelItems;
    }
}

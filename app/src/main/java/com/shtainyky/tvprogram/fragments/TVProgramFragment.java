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
import com.shtainyky.tvprogram.adapter.TabPagerFragmentAdapter;

public class TVProgramFragment extends Fragment {
    public static final String ARG_PREFERRED = "is_preferred";
    private View view;
    private TabPagerFragmentAdapter mAdapter;
    boolean mIsPreferred;


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
        view = inflater.inflate(R.layout.fragment_tvprograms, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mIsPreferred = bundle.getBoolean(ARG_PREFERRED);
        }

        setupTabs(mIsPreferred);
        return view;
    }

    private void setupTabs(boolean isPreferred) {
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        mAdapter = new TabPagerFragmentAdapter(getContext(), getActivity().getSupportFragmentManager(), isPreferred);
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}

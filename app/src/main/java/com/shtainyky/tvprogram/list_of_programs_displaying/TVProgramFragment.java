package com.shtainyky.tvprogram.list_of_programs_displaying;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.list_of_programs_displaying.adapters.TabPagerFragmentAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TVProgramFragment extends Fragment {
    public static final String ARG_PREFERRED = "is_preferred";
    boolean mIsPreferred;

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

        setupTabs(mIsPreferred);
        return view;
    }

    private void setupTabs(boolean isPreferred) {
        TabPagerFragmentAdapter mAdapter = new TabPagerFragmentAdapter(getContext(), getActivity().getSupportFragmentManager(), isPreferred);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

}

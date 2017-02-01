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
import com.shtainyky.tvprogram.database.DatabaseSource;

import java.util.List;

public class TVProgramFragment extends Fragment {
    public static final String ARG_PREFERRED = "is_preferred";
    private View view;
    private TabPagerFragmentAdapter mAdapter;
    private DatabaseSource mSource;
    private List<String> list;
    private int arg;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tvprograms, container, false);
        mSource = new DatabaseSource(getContext());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            arg = bundle.getInt(ARG_PREFERRED);
        }
        if (arg == 0){
            list = mSource.getAllChannelsTitles();
        }
        else {
            list = mSource.getPreferredChannelsTitles();
        }
        setupTabs(list);
        return view;
    }


    private void setupTabs(List<String> titlesList) {
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        mAdapter = new TabPagerFragmentAdapter(getContext(), getActivity().getSupportFragmentManager(), titlesList);
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}

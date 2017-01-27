package com.shtainyky.tvprogram;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TVProgramViewPagerFragment extends Fragment {
    private int position;
    private RecyclerView mCrimeRecyclerView;

    public TVProgramViewPagerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vp_tvprograms, container, false);
        TextView textView = (TextView) view.findViewById(R.id.vp_textView);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            position = bundle.getInt("position");
            String temp = "Position = " + position;
            textView.setText(temp);
        }

        mCrimeRecyclerView = (RecyclerView) view
                .findViewById(R.id.tvprogram_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));

        return view;
    }
}

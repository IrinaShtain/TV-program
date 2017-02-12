package com.shtainyky.tvprogram.listofchannelspacage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.model.ChannelItem;

import java.util.ArrayList;
import java.util.List;

public class ListOfChannelsFragment extends Fragment {
    public static final String ARG_CATEGORY_ID = "categoryId";

    private RecyclerView mChannelsRecyclerView;
    private DatabaseSource mSource;
    private int categoryId;

    public static ListOfChannelsFragment newInstance(int categoryId) {
        ListOfChannelsFragment fragment = new ListOfChannelsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_CATEGORY_ID, categoryId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_of_channels, container, false);
        mSource = new DatabaseSource(getContext());
        mChannelsRecyclerView = (RecyclerView) view
                .findViewById(R.id.list_of_channels_recycler_view);
        mChannelsRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            categoryId = bundle.getInt(ARG_CATEGORY_ID);
        }
        requestData(categoryId);
        return view;
    }


    public void requestData(int categoryId) {
        List<ChannelItem> mChannels;
        if (categoryId == 0) {
            mChannels = mSource.getAllChannel();
        } else {
            mChannels = mSource.getChannelsForCategory(categoryId);
        }
        ListOfChannelsAdapter mAdapter = new ListOfChannelsAdapter(getContext(), mChannels);
        mChannelsRecyclerView.setAdapter(mAdapter);
    }
}

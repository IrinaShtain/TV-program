package com.shtainyky.tvprogram.navigationdrawerfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.model.Channel;

import java.util.ArrayList;
import java.util.List;

public class ListOfChannelsFragment extends Fragment {

    private RecyclerView mChannelsRecyclerView;
    private ListOfChannelsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_of_channels, container, false);

        mChannelsRecyclerView = (RecyclerView) view
                .findViewById(R.id.list_of_channels_recycler_view);
        mChannelsRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));

        //will be fixed
        List<Channel> channels = new ArrayList<>();
        mAdapter = new ListOfChannelsAdapter(channels);
        mChannelsRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private class ListOfChannelsHolder extends RecyclerView.ViewHolder {
        TextView mChannelNameTextView;
        TextView mChannelCategoryTextView;
        ImageView mImageView;

        ListOfChannelsHolder(View itemView) {
            super(itemView);
            mChannelNameTextView = (TextView) itemView.findViewById(R.id.channel_name);
            mChannelCategoryTextView = (TextView) itemView.findViewById(R.id.channel_category);
            mImageView = (ImageView) itemView.findViewById(R.id.channel_logo);
        }

        public void bindChannel(Channel channel) {
            mChannelNameTextView.setText(channel.getName());
            mChannelCategoryTextView.setText(channel.getCategory_id());

        }
    }

    private class ListOfChannelsAdapter extends RecyclerView.Adapter<ListOfChannelsFragment.ListOfChannelsHolder> {
        private List<Channel> mChannels;

        public ListOfChannelsAdapter(List<Channel> channels) {
            mChannels = channels;
        }

        @Override
        public ListOfChannelsFragment.ListOfChannelsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_channel, parent, false);
            return new ListOfChannelsFragment.ListOfChannelsHolder(view);
        }

        @Override
        public void onBindViewHolder(ListOfChannelsFragment.ListOfChannelsHolder holder, int position) {
            Channel channel = mChannels.get(position);
            holder.bindChannel(channel);
        }

        @Override
        public int getItemCount() {
            return mChannels.size();
        }
    }

}

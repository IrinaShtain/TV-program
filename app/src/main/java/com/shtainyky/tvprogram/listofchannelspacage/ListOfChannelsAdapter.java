package com.shtainyky.tvprogram.listofchannelspacage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.model.ChannelItem;

import java.util.List;

class ListOfChannelsAdapter extends RecyclerView.Adapter<ListOfChannelsHolder> {
    private List<ChannelItem> mChannels;
    private Context mContext;

    ListOfChannelsAdapter(Context context, List<ChannelItem> channels) {
        mContext = context;
        mChannels = channels;
    }

    @Override
    public ListOfChannelsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.item_channel, parent, false);
        return new ListOfChannelsHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(ListOfChannelsHolder holder, int position) {
        ChannelItem channel = mChannels.get(position);
        holder.bindChannel(channel);
    }

    @Override
    public int getItemCount() {
        return mChannels.size();
    }
}

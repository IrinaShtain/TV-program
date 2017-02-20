package com.shtainyky.tvprogram.list_of_channels_displaying;

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
    private int mFromFlag;

    ListOfChannelsAdapter(Context context, List<ChannelItem> channels, int fromFlag) {
        mContext = context;
        mChannels = channels;
        mFromFlag = fromFlag;
    }

    @Override
    public ListOfChannelsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_channel, parent, false);
        return new ListOfChannelsHolder(mContext, view, mFromFlag);
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

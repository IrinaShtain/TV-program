package com.shtainyky.tvprogram.fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.model.Channel;
import com.shtainyky.tvprogram.parser.Parse;
import com.shtainyky.tvprogram.utils.Constants;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class ListOfPreferredChannelsFragment extends Fragment {
    private DatabaseSource mSource;
    private AVLoadingIndicatorView mProgressBar;
    private ListOfPreferredChannelsAdapter mAdapter;
    private List<Channel> mChannels = new ArrayList<>();
    private RecyclerView mChannelsRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_of_channels, container, false);
        mSource = new DatabaseSource(getContext());
        mProgressBar = (AVLoadingIndicatorView) view.findViewById(R.id.progress);
        mChannelsRecyclerView = (RecyclerView) view
                .findViewById(R.id.list_of_channels_recycler_view);
        mChannelsRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));
        requestData();
        return view;
    }

    public void requestData() {
        new MyPreferredChannelsTask().execute();
    }

    @Override
    public void onStart() {
        requestData();
        super.onStart();
    }

    private class MyPreferredChannelsTask extends AsyncTask<Void, Void, List<Channel>> {
        @Override
        protected List<Channel> doInBackground(Void... params) {
            mChannels = mSource.getPreferredChannels();
            return mChannels;
        }

        @Override
        protected void onPostExecute(List<Channel> channels) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mAdapter = new ListOfPreferredChannelsAdapter(channels);
            mChannelsRecyclerView.setAdapter(mAdapter);
        }
    }

    private class ListOfPreferredChannelsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mChannelNameTextView;
        TextView mChannelCategoryTextView;
        ImageView mImageView;
        String mChannelName;
        private int mChannelId;


        ListOfPreferredChannelsHolder(View itemView) {
            super(itemView);
            mChannelNameTextView = (TextView) itemView.findViewById(R.id.channel_name);
            mChannelNameTextView.setOnClickListener(this);
            mChannelCategoryTextView = (TextView) itemView.findViewById(R.id.channel_category);
            mChannelCategoryTextView.setOnClickListener(this);
            mImageView = (ImageView) itemView.findViewById(R.id.channel_logo);
            mImageView.setOnClickListener(this);
        }

        public void bindChannel(Channel channel) {
            mChannelName = channel.getName();
            mChannelId = channel.getId();
            mChannelNameTextView.setText(channel.getName());
            mChannelCategoryTextView.setText(String.valueOf(channel.getCategory_name()));
            String imageName = Constants.CHANNEL_IMAGE + channel.getId() + Constants.PNG;
            Parse.loadImageBitmapFromStorage(getContext(), imageName, mImageView);
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Please, choose")
                    .setMessage(getResources().getString(R.string.question_delete_preferred, mChannelName))
                    .setCancelable(false)
                    .setNegativeButton(getResources().getString(R.string.answer_no),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getContext(), R.string.cancel, Toast.LENGTH_SHORT).show();
                                }
                            })
                    .setPositiveButton(getResources().getString(R.string.answer_yes),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mSource.setChannelPreferred(mChannelId, 0);
                                    Toast.makeText(getContext(),
                                            getResources().getString(R.string.delete_channel_is_preferred, mChannelName),
                                            Toast.LENGTH_LONG).show();
                                    requestData();


                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private class ListOfPreferredChannelsAdapter extends RecyclerView.Adapter<ListOfPreferredChannelsHolder> {
        private List<Channel> mChannels;

        public ListOfPreferredChannelsAdapter(List<Channel> channels) {
            mChannels = channels;
        }

        @Override
        public ListOfPreferredChannelsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_channel, parent, false);
            return new ListOfPreferredChannelsHolder(view);
        }

        @Override
        public void onBindViewHolder(ListOfPreferredChannelsHolder holder, int position) {
            Channel channel = mChannels.get(position);
            holder.bindChannel(channel);
        }

        @Override
        public int getItemCount() {
            return mChannels.size();
        }
    }
}

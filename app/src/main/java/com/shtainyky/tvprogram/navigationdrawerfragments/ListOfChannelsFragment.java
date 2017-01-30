package com.shtainyky.tvprogram.navigationdrawerfragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.*;

public class ListOfChannelsFragment extends Fragment {

    private RecyclerView mChannelsRecyclerView;
    private ProgressBar mProgressBar;
    private ListOfChannelsAdapter mAdapter;
    private List<Channel> mChannels = new ArrayList<>();
    private DatabaseSource mSource;
    private int categoryId;
    private Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_of_channels, container, false);
        mSource = new DatabaseSource(getContext());
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);

        mChannelsRecyclerView = (RecyclerView) view
                .findViewById(R.id.list_of_channels_recycler_view);
        mChannelsRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));
        bundle = this.getArguments();
        if (bundle != null) {
            categoryId = bundle.getInt("categoryId");
        }
        requestData(categoryId);
        return view;
    }

    @Override
    public void onStart() {
        requestData(categoryId);
        super.onStart();
    }

    public void requestData(int categoryId) {
        new MyChannelsTask().execute(categoryId);
    }

    private class MyChannelsTask extends AsyncTask<Integer, Void, List<Channel>> {
        @Override
        protected List<Channel> doInBackground(Integer... params) {
            if (params[0] == 0) {
                mChannels = mSource.getAllChannel();
            }
            else {
                mChannels = mSource.getChannelsForCategory(params[0]);
            }
            Log.i("myLog", "MyChannelsTask");
            return mChannels;
        }

        @Override
        protected void onPostExecute(List<Channel> channels) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mAdapter = new ListOfChannelsAdapter(channels);
            mChannelsRecyclerView.setAdapter(mAdapter);
        }
    }

    private class ListOfChannelsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mChannelNameTextView;
        TextView mChannelCategoryTextView;
        ImageView mImageView;
        String mChannelName;
        int mChannelId;
        int mChannelPreferred;

        ListOfChannelsHolder(View itemView) {
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
            mChannelPreferred = channel.getIs_preferred();
            mChannelNameTextView.setText(channel.getName());
            mChannelCategoryTextView.setText(String.valueOf(channel.getCategory_name()));
            String imageName = Constants.CHANNEL_IMAGE + channel.getId() + Constants.PNG;
            Parse.loadImageBitmapFromStorage(getContext(), imageName, mImageView);
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Please, choose")
                    .setMessage(getResources().getString(R.string.question_add_preferred, mChannelName))
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
                                    if (mChannelPreferred == 1)
                                    {
                                        Toast.makeText(getContext(),
                                                getResources().getString(R.string.channel_is_preferred, mChannelName),
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        mSource.setChannelPreferred(mChannelId, 1);
                                        Toast.makeText(getContext(),
                                                getResources().getString(R.string.channel_will_be_preferred, mChannelName),
                                                Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
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

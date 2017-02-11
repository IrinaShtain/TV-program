package com.shtainyky.tvprogram.fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.model.ChannelItem;
import com.shtainyky.tvprogram.parser.Parse;
import com.shtainyky.tvprogram.utils.Constants;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import static com.shtainyky.tvprogram.fragments.ListOfChannelsFragment.ARG_CATEGORY_ID;
import static com.shtainyky.tvprogram.fragments.TVProgramFragment.ARG_PREFERRED;

public class ListOfPreferredChannelsFragment extends Fragment {
    private DatabaseSource mSource;
    private AVLoadingIndicatorView mProgressBar;
    private ListOfPreferredChannelsAdapter mAdapter;
    private List<ChannelItem> mChannels = new ArrayList<>();
    private RecyclerView mChannelsRecyclerView;
    private Button mButtonPreferred;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_list_of_channels, container, false);
        // setupButton();
        mSource = new DatabaseSource(getContext());
        mProgressBar = (AVLoadingIndicatorView) mView.findViewById(R.id.progress);
        mChannelsRecyclerView = (RecyclerView) mView
                .findViewById(R.id.list_of_channels_recycler_view);
        mChannelsRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));
        requestData();
        return mView;
    }

    public void requestData() {
        new MyPreferredChannelsTask().execute();
    }


    public void setupButton() {
        mButtonPreferred = (Button) mView.findViewById(R.id.buttonPreferred);
        mButtonPreferred.setVisibility(View.VISIBLE);
        if (mChannels.size() != 0) {
            mButtonPreferred.setText(R.string.show_preferred);
            mButtonPreferred.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setFragment(TVProgramFragment.newInstance(1));
                }
            });
        } else {
            mButtonPreferred.setText(R.string.show_nothing);
            mButtonPreferred.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setFragment(ListOfChannelsFragment.newInstance(0));
                }
            });
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
        Fragment mFragment = mFragmentManager.findFragmentById(R.id.fragment_container);
        if (mFragment == null) {
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }

    }

    private class MyPreferredChannelsTask extends AsyncTask<Void, Void, List<ChannelItem>> {
        @Override
        protected List<ChannelItem> doInBackground(Void... params) {
            mChannels = mSource.getPreferredChannels();
            return mChannels;
        }

        @Override
        protected void onPostExecute(List<ChannelItem> channels) {
            mAdapter = new ListOfPreferredChannelsAdapter(channels);
            mChannelsRecyclerView.setAdapter(mAdapter);
            setupButton();
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

        void bindChannel(ChannelItem channel) {
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
        private List<ChannelItem> mChannels;

        ListOfPreferredChannelsAdapter(List<ChannelItem> channels) {
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
            ChannelItem channel = mChannels.get(position);
            holder.bindChannel(channel);
        }

        @Override
        public int getItemCount() {
            return mChannels.size();
        }
    }
}

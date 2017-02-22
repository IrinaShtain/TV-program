package com.shtainyky.tvprogram.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.adapters.CategoriesRecyclerViewAdapter;
import com.shtainyky.tvprogram.adapters.ChannelsRecyclerViewAdapter;
import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.model.ChannelItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListOfChannelsFragment extends Fragment implements ChannelsRecyclerViewAdapter.OnChannelClickListener {
    public static final String ARG_CATEGORY_ID = "mCategoryId";
    public static final String ARG_IS_PREFERRED = "is_preferred";

    private DatabaseSource mSource;
    private int mCategoryId;
    private boolean mIsPreferred;
    private View mView;
    private List<ChannelItem> mChannels;
    private CategoriesRecyclerViewAdapter.OnCategoryClickListener mOnCategoryClickListener;
    private OnPreferredChannelClickListener mOnPreferredChannelClickListener;
    private ChannelsRecyclerViewAdapter mAdapter;

    @BindView(R.id.list_of_channels_recycler_view)
    RecyclerView mChannelsRecyclerView;

    public static ListOfChannelsFragment newInstance(int categoryId, boolean isPreferred) {
        ListOfChannelsFragment fragment = new ListOfChannelsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_CATEGORY_ID, categoryId);
        bundle.putBoolean(ARG_IS_PREFERRED, isPreferred);
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface OnPreferredChannelClickListener {
        void onPreferredChannelClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = null;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
        try {
            mOnCategoryClickListener = (CategoriesRecyclerViewAdapter.OnCategoryClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCategoryClickListener");
        }
        try {
            mOnPreferredChannelClickListener = (OnPreferredChannelClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnPreferredChannelClickListener");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_list_of_channels, container, false);
        ButterKnife.bind(this, mView);

        mSource = new DatabaseSource(getContext());
        getBundle();
        showChannels();
        return mView;
    }

    private void getBundle() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mCategoryId = bundle.getInt(ARG_CATEGORY_ID);
            mIsPreferred = bundle.getBoolean(ARG_IS_PREFERRED);
        }
    }

    private void showChannels() {
        if (!mIsPreferred)
            showAllChannelsOrChannelsForCategory(mCategoryId);
        else
            showPreferredChannels();
        mChannelsRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));
        mAdapter.setOnChannelClickListener(this);
        mChannelsRecyclerView.setAdapter(mAdapter);

    }

    private void showPreferredChannels() {
        Button buttonPreferred = (Button) mView.findViewById(R.id.buttonPreferred);
        buttonPreferred.setVisibility(View.VISIBLE);

        mChannels = mSource.getPreferredChannels();

        mAdapter = new ChannelsRecyclerViewAdapter(getContext(), mChannels);
        if (mChannels.size() != 0) {
            buttonPreferred.setText(R.string.show_preferred);
            buttonPreferred.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPreferredChannelClickListener.onPreferredChannelClick();
                }
            });
        } else {
            buttonPreferred.setText(R.string.show_nothing);
            buttonPreferred.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnCategoryClickListener.onCategoryClick(0);
                }
            });
        }
    }

    public void showAllChannelsOrChannelsForCategory(int categoryId) {
        if (categoryId == 0) {
            mChannels = mSource.getAllChannel();
        } else {
            mChannels = mSource.getChannelsForCategory(categoryId);
        }
        mAdapter = new ChannelsRecyclerViewAdapter(getContext(), mChannels);
    }

    @Override
    public void onChannelClick(int channelPosition) {
        ChannelItem channelItem = mChannels.get(channelPosition);
        showAlertDialog(channelItem);
    }

    private void showAlertDialog(final ChannelItem channelItem) {
        final boolean isPreferred = channelItem.getIs_preferred() == 1;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.selecting_title)
                .setMessage(getQuestionForAlertDialog(isPreferred, channelItem.getName()))
                .setCancelable(false)
                .setNegativeButton(getContext().getResources().getString(R.string.answer_no),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                .setPositiveButton(getContext().getResources().getString(R.string.answer_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (isPreferred) {
                                    setChannelNotPreferred(channelItem);
                                } else {
                                    setChannelPreferred(channelItem);
                                }
                                //TODO: mAdapter.notifyDataSetChanged() didn't work
                                showChannels();

                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void setChannelNotPreferred(ChannelItem channelItem) {
        mSource.setChannelPreferred(channelItem.getId(), 0);
        Toast.makeText(getContext(),
                getContext().getResources().getString(R.string.delete_channel_is_preferred, channelItem.getName()),
                Toast.LENGTH_LONG).show();
    }

    private void setChannelPreferred(ChannelItem channelItem) {
        mSource.setChannelPreferred(channelItem.getId(), 1);
        Toast.makeText(getContext(),
                getContext().getResources().getString(R.string.channel_will_be_preferred, channelItem.getName()),
                Toast.LENGTH_LONG).show();
    }

    private String getQuestionForAlertDialog(boolean isPreferred, String channelName) {
        if (!isPreferred)
            return getContext().getResources().getString(R.string.question_add_preferred, channelName);
        else {
            return getContext().getResources().getString(R.string.question_delete_preferred, channelName);
        }
    }


}

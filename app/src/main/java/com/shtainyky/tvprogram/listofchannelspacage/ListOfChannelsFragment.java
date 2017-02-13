package com.shtainyky.tvprogram.listofchannelspacage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.listofcategoriespacage.ChannelListener;
import com.shtainyky.tvprogram.model.ChannelItem;

import java.util.List;

public class ListOfChannelsFragment extends Fragment {
    public static final String ARG_CATEGORY_ID = "categoryId";
    public static final String ARG_IS_PREFERRED = "is_prefereed";
    public static final int FLAG_PREFERRED = 1;
    public static final int FLAG_NOT_PREFERRED = 0;

    private DatabaseSource mSource;
    private int categoryId;
    private boolean isPreferred;
    private View mView;
    private List<ChannelItem> mChannels;
    private ChannelListener mCategoryListener;
    private PreferredChannelListener mPreferredChannelListener;
    private ListOfChannelsAdapter mAdapter;

    public static ListOfChannelsFragment newInstance(int categoryId, boolean isPreferred) {
        ListOfChannelsFragment fragment = new ListOfChannelsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_CATEGORY_ID, categoryId);
        bundle.putBoolean(ARG_IS_PREFERRED, isPreferred);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_list_of_channels, container, false);
        mSource = new DatabaseSource(getContext());
        RecyclerView mChannelsRecyclerView = (RecyclerView) mView
                .findViewById(R.id.list_of_channels_recycler_view);
        mChannelsRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            categoryId = bundle.getInt(ARG_CATEGORY_ID);
            isPreferred = bundle.getBoolean(ARG_IS_PREFERRED);
        }
        if (!isPreferred)
            showChannels(categoryId);
        else
            showPreferredChannels();
        mChannelsRecyclerView.setAdapter(mAdapter);
        return mView;
    }

    private void showPreferredChannels() {
        Button mButtonPreferred = (Button) mView.findViewById(R.id.buttonPreferred);
        mButtonPreferred.setVisibility(View.VISIBLE);
        mChannels = mSource.getPreferredChannels();
        if (!(getContext() instanceof ChannelListener))
            throw new AssertionError("Your class must implement ChannelListener");
        if (!(getContext() instanceof PreferredChannelListener))
            throw new AssertionError("Your class must implement PreferredChannelListener");
        mCategoryListener = (ChannelListener) getContext();
        mPreferredChannelListener = (PreferredChannelListener) getContext();
        mAdapter = new ListOfChannelsAdapter(getContext(), mChannels, FLAG_PREFERRED);
        if (mChannels.size() != 0) {
            mButtonPreferred.setText(R.string.show_preferred);
            mButtonPreferred.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "mChannels.size() = " + mChannels.size(), Toast.LENGTH_SHORT).show();
                    mPreferredChannelListener.showPreferredChannels();
                }
            });
        } else {
            mButtonPreferred.setText(R.string.show_nothing);
            mButtonPreferred.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCategoryListener.setChannelsForCategoryId(0, true);
                }
            });
        }
    }


    public void showChannels(int categoryId) {
        if (categoryId == 0) {
            mChannels = mSource.getAllChannel();
        } else {
            mChannels = mSource.getChannelsForCategory(categoryId);
        }
        mAdapter = new ListOfChannelsAdapter(getContext(), mChannels, FLAG_NOT_PREFERRED);
    }
}

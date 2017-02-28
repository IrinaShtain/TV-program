package com.shtainyky.tvprogram.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.adapters.TabPagerFragmentAdapter;
import com.shtainyky.tvprogram.database.ContractClass;
import com.shtainyky.tvprogram.database.DatabaseStoreImp;
import com.shtainyky.tvprogram.model.ChannelItem;
import com.shtainyky.tvprogram.models.models_ui.ChannelUI;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shtainyky.tvprogram.database.ContractClass.Channels.COLUMN_CHANNEL_IS_PREFERRED;

public class TVProgramFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    public static final String ARG_PREFERRED = "is_preferred";
    private static final String ARG_PREFERRED_FOR_LOADERS = "is_preferred_loading";
    private boolean mIsPreferred;
    private TabPagerFragmentAdapter mAdapter;
    private List<ChannelUI> mChannelUIList = new ArrayList<>();
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.progress)
    AVLoadingIndicatorView mProgress;


    public static TVProgramFragment newInstance(boolean preferredFlag) {
        TVProgramFragment fragment = new TVProgramFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_PREFERRED, preferredFlag);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tvprograms, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mIsPreferred = bundle.getBoolean(ARG_PREFERRED);
        }
        mProgress.show();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupAndInitLoader();
        setupTabs();
    }

    private void setupTabs() {
        mAdapter = new TabPagerFragmentAdapter(getActivity().getSupportFragmentManager(),
                null);

        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setupAndInitLoader() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_PREFERRED_FOR_LOADERS, mIsPreferred);
        getLoaderManager().initLoader(1, bundle, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri CONTACT_URI = Uri.parse(String.valueOf(ContractClass.Channels.CONTENT_URI));
        boolean isPreferred = false;
        if (args != null) {
            isPreferred = args.getBoolean(ARG_PREFERRED_FOR_LOADERS);
        }
        if (isPreferred)
            return new CursorLoader(getContext(),
                    CONTACT_URI,
                    ContractClass.Channels.DEFAULT_PROJECTION,
                    COLUMN_CHANNEL_IS_PREFERRED + "=?",
                    new String[]{"" + 1},
                    ContractClass.Channels.COLUMN_CHANNEL_TITLE + " ASC ");
        else
            return new CursorLoader(getContext(),
                    CONTACT_URI,
                    ContractClass.Channels.DEFAULT_PROJECTION,
                    null,
                    null,
                    ContractClass.Channels.COLUMN_CHANNEL_TITLE + " ASC ");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.isClosed()) mChannelUIList = ChannelUI.getListOfChannelsForUI(data) ;
        mAdapter.swapListOfChannels(mChannelUIList);
        mProgress.hide();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapListOfChannels(null);
    }
}

package com.shtainyky.tvprogram.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.adapters.TVProgramRecyclerViewAdapter;
import com.shtainyky.tvprogram.database.ContractClass;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TVProgramViewPagerFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final int REQUEST_DATE = 0;
    public static final String ARG_POSITION = "position";
    public static final String ARG_CHANNEL_ID = "channel_ID";
    public static final String ARG_DATE = "date";

    private int mChannelId;
    private String mForDate;
    private SimpleDateFormat simpleDateFormat;

    @BindView(R.id.tvprogram_recycler_view)
    RecyclerView mTVProgramRecyclerView;

    @BindView(R.id.buttonDate)
    Button mGetDateButton;

    @BindView(R.id.progress)
    AVLoadingIndicatorView mProgress;

    public TVProgramViewPagerFragment() {
    }

    public static TVProgramViewPagerFragment newInstance(int position) {
        TVProgramViewPagerFragment fragment = new TVProgramViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vp_tvprograms, container, false);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        ButterKnife.bind(this, view);
        startAnim();

        setupButtonDate();
        setupUI();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mChannelId = bundle.getInt(ARG_POSITION);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupAndInitLoader(mForDate, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mGetDateButton.setText(simpleDateFormat.format(date));
            setupUI();
            String dateDay = simpleDateFormat.format(date).split("/")[0];
            setupAndInitLoader(mForDate, Integer.parseInt(dateDay));
            Log.i("myLog", mForDate);
        }
    }


    public void setupButtonDate() {
        mGetDateButton.setText(simpleDateFormat.format(new Date()));
        mGetDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dialog = new DatePickerFragment();
                dialog.setTargetFragment(TVProgramViewPagerFragment.this, REQUEST_DATE);
                dialog.show(getFragmentManager(), "TVProgramViewPagerFragment");
            }
        });
    }

    public void setupUI() {
        mForDate = String.valueOf(mGetDateButton.getText());
        mTVProgramRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        TVProgramRecyclerViewAdapter mAdapter = new TVProgramRecyclerViewAdapter(getContext(), null);
        mTVProgramRecyclerView.setAdapter(mAdapter);
    }

    private void setupAndInitLoader(String forDate, int loaderId) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_CHANNEL_ID, mChannelId);
        bundle.putString(ARG_DATE, forDate);
        getLoaderManager().initLoader(loaderId, bundle, this);
    }

    private void startAnim() {
        mProgress.show();
    }

    private void stopAnim() {
        mProgress.hide();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i("myLog", "onCreateLoader =");
        String forDate = args.getString(ARG_DATE, "no");
        int channelID = args.getInt(ARG_CHANNEL_ID, 1);
        Uri CONTACT_URI = Uri.parse(String.valueOf(ContractClass.Programs.CONTENT_URI));
        return new CursorLoader(getContext(), CONTACT_URI, ContractClass.Programs.DEFAULT_PROJECTION,
                ContractClass.Programs.COLUMN_PROGRAM_CHANNEL_ID + " = ? AND "
                        + ContractClass.Programs.COLUMN_PROGRAM_DATE + " = ?",
                new String[]{String.valueOf(channelID), forDate}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ((TVProgramRecyclerViewAdapter) mTVProgramRecyclerView.getAdapter()).swapCursor(data);
        stopAnim();
        Log.i("myLog", "onLoadFinished = TVProgramRecyclerViewAdapter");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((TVProgramRecyclerViewAdapter) mTVProgramRecyclerView.getAdapter()).swapCursor(null);
    }
}

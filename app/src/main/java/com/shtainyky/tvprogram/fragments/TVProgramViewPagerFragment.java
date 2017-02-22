package com.shtainyky.tvprogram.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.adapters.TVProgramRecyclerViewAdapter;
import com.shtainyky.tvprogram.model.ProgramItem;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TVProgramViewPagerFragment extends Fragment {
    private static final int REQUEST_DATE = 0;
    public static final String ARG_POSITION = "position";

    private int mChannelId;
    private String mForDate;
    private DatabaseSource mSource;
    private List<ProgramItem> mPrograms;

    @BindView(R.id.tvprogram_recycler_view)
    RecyclerView mTVProgramRecyclerView;

    @BindView(R.id.buttonDate)
    Button mGetDateButton;

    @BindView(R.id.progress)
    AVLoadingIndicatorView mProgress;

    public TVProgramViewPagerFragment() {}

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
        ButterKnife.bind(this, view);
        mPrograms = new ArrayList<>();
        mSource = new DatabaseSource(getContext());

        setupButtonDate();
        setupUI();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mChannelId = bundle.getInt(ARG_POSITION);
            showProgram(mForDate);
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            // TODO: 20.02.17 check SimpleDateFormat
            mGetDateButton.setText(DateFormat.format("dd/MM/yyyy", date));
            setupUI();
            showProgram(String.valueOf(DateFormat.format("dd/MM/yyyy", date)));
            Log.i("myLog", mForDate);
        }
    }


    public void setupButtonDate() {
        mGetDateButton.setText(DateFormat.format("dd/MM/yyyy", Calendar.getInstance()));
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
    }

    private void showProgram(String forDate) {
        startAnim();
        mPrograms = mSource.getPrograms(mChannelId, forDate);
        stopAnim();
        TVProgramRecyclerViewAdapter mAdapter = new TVProgramRecyclerViewAdapter(getContext(), mPrograms);
        mTVProgramRecyclerView.setAdapter(mAdapter);
    }

    void startAnim() {
        mProgress.show();
    }

    void stopAnim() {
        mProgress.hide();
    }

}
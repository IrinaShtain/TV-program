package com.shtainyky.tvprogram.list_of_programs_displaying;

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
import com.shtainyky.tvprogram.model.ProgramItem;
import com.shtainyky.tvprogram.list_of_programs_displaying.adapters.TVProgramAdapter;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TVProgramViewPagerFragment extends Fragment {
    private static final int REQUEST_DATE = 0;
    public static final String ARG_POSITION = "position";
    private int channelId;
    private String forDate;
    private RecyclerView mTVProgramRecyclerView;
    private List<ProgramItem> mPrograms;
    private Button mGetDateButton;
    private DatabaseSource mSource;
    private AVLoadingIndicatorView mProgress;
    private View view;

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
        view = inflater.inflate(R.layout.fragment_vp_tvprograms, container, false);
        mPrograms = new ArrayList<>();
        mSource = new DatabaseSource(getContext());
        mProgress = (AVLoadingIndicatorView) view.findViewById(R.id.progress);
        mGetDateButton = (Button) view.findViewById(R.id.buttonDate);

        setupButtonDate();
        setupUI();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            channelId = bundle.getInt(ARG_POSITION);
            showProgram(forDate);
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
            mGetDateButton.setText(DateFormat.format("dd/MM/yyyy", date));
            setupUI();
            showProgram(String.valueOf(DateFormat.format("dd/MM/yyyy", date)));
            Log.i("myLog", forDate);
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
        forDate = String.valueOf(mGetDateButton.getText());
        mTVProgramRecyclerView = (RecyclerView) view
                .findViewById(R.id.tvprogram_recycler_view);
        mTVProgramRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));
    }

    private void showProgram(String forDate) {
        startAnim();
        mPrograms = mSource.getPrograms(channelId, forDate);
        stopAnim();
        TVProgramAdapter mAdapter = new TVProgramAdapter(getContext(), mPrograms);
        mTVProgramRecyclerView.setAdapter(mAdapter);
    }

    void startAnim() {
        mProgress.show();
    }

    void stopAnim() {
        mProgress.hide();
    }

}

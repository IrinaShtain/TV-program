package com.shtainyky.tvprogram.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.TextView;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.model.Program;
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
    private TVProgramAdapter mAdapter;
    private List<Program> mPrograms;
    private Button mGetDateButton;
    private DatabaseSource mSource;
    private AVLoadingIndicatorView mProgress;
    private View view;

    public TVProgramViewPagerFragment() {

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
            requestData(channelId, forDate);
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
            requestData(channelId, String.valueOf(DateFormat.format("dd/MM/yyyy", date)));
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
                requestData(channelId, forDate);
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

    private void requestData(int ChannelID, String forDate) {
        new MyTVProgramTask().execute(String.valueOf(ChannelID), forDate);
    }

    void startAnim() {
        mProgress.show();
    }

    void stopAnim() {
        mProgress.hide();
    }


    public class MyTVProgramTask extends AsyncTask<String, Void, List<Program>> {
        @Override
        protected void onPreExecute() {
            startAnim();
        }

        @Override
        protected List<Program> doInBackground(String... params) {
            mPrograms = mSource.getPrograms(Integer.parseInt(params[0]), params[1]);
            Log.i("myLog", "MyTVProgramTask = size" + mPrograms.size());
            Log.i("myLog", "MyTVProgramTask = params[1]" + params[1]);
            return mPrograms;
        }

        @Override
        protected void onPostExecute(List<Program> programs) {
            stopAnim();
            mAdapter = new TVProgramAdapter(programs);
            mTVProgramRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }


    private class TVProgramHolder extends RecyclerView.ViewHolder {
        TextView mTitleTextView;
        TextView mTimeTextView;
        TextView mDateTextView;

        TVProgramHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.title);
            mTimeTextView = (TextView) itemView.findViewById(R.id.time);
            mDateTextView = (TextView) itemView.findViewById(R.id.date);
        }

        public void bindProgram(Program program) {
            mTitleTextView.setText(program.getTitle());
            mTimeTextView.setText(program.getTime());
            mDateTextView.setText(program.getDate());
        }
    }

    private class TVProgramAdapter extends RecyclerView.Adapter<TVProgramHolder> {
        private List<Program> mPrograms;

        public TVProgramAdapter(List<Program> programs) {
            mPrograms = programs;
        }

        @Override
        public TVProgramHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_program, parent, false);
            return new TVProgramHolder(view);
        }

        @Override
        public void onBindViewHolder(TVProgramHolder holder, int position) {
            Program program = mPrograms.get(position);
            holder.bindProgram(program);
        }

        @Override
        public int getItemCount() {
            return mPrograms.size();
        }
    }
}

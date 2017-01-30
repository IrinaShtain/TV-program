package com.shtainyky.tvprogram.navigationdrawerfragments;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.utils.HttpManager;
import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.model.Program;
import com.shtainyky.tvprogram.parser.Parse;
import com.shtainyky.tvprogram.utils.Constants;
import com.shtainyky.tvprogram.utils.SomeMethods;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class TVProgramViewPagerFragment extends Fragment {
    private int channelId;
    private String forDate;
    private RecyclerView mTVProgramRecyclerView;
    private TVProgramAdapter mAdapter;
    private List<Program> mPrograms;
    private Button mGetDateButton;
    private DatabaseSource mSource;
    private ProgressBar mProgress;

    public TVProgramViewPagerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vp_tvprograms, container, false);
        mSource = new DatabaseSource(getContext());
        mProgress = (ProgressBar) view.findViewById(R.id.progress);
        mGetDateButton = (Button) view.findViewById(R.id.buttonDate);
        mGetDateButton.setText(DateFormat.format("dd/MM/yyyy ", Calendar.getInstance()));
        forDate = String.valueOf(mGetDateButton.getText());
        mPrograms = new ArrayList<>();
        mTVProgramRecyclerView = (RecyclerView) view
                .findViewById(R.id.tvprogram_recycler_view);
        mTVProgramRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            channelId = bundle.getInt("position");
            requestData(channelId, forDate);
        }
        return view;
    }

    private void requestData(int ChannelID, String forDate) {
        new MyTask().execute(String.valueOf(ChannelID), forDate);
    }


    public class MyTask extends AsyncTask<String, Void, List<Program>> {
        @Override
        protected List<Program> doInBackground(String... params) {
            mPrograms = mSource.getPrograms(Integer.parseInt(params[0]), params[1]);
            Log.i("myLog", "mPrograms = " + mPrograms.size());
            Log.i("myLog", "setTitle = " + channelId + "forDate = " + forDate);
            return mPrograms;
        }

        @Override
        protected void onPostExecute(List<Program> programs) {
            mProgress.setVisibility(View.INVISIBLE);
            mAdapter = new TVProgramAdapter(programs);
            mTVProgramRecyclerView.setAdapter(mAdapter);
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

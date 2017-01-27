package com.shtainyky.tvprogram.navigationdrawerfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.model.Program;

import java.util.ArrayList;
import java.util.List;

public class TVProgramViewPagerFragment extends Fragment {
    private int position;
    private RecyclerView mTVProgramRecyclerView;
    private TVProgramAdapter mAdapter;

    public TVProgramViewPagerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vp_tvprograms, container, false);
        TextView textView = (TextView) view.findViewById(R.id.vp_textView);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            position = bundle.getInt("position");
            String temp = "Position = " + position;
            textView.setText(temp);
        }

        mTVProgramRecyclerView = (RecyclerView) view
                .findViewById(R.id.tvprogram_recycler_view);
        mTVProgramRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));

        //will be fixed
        List<Program> programs = new ArrayList<>();
        mAdapter = new TVProgramAdapter(programs);
        mTVProgramRecyclerView.setAdapter(mAdapter);


        return view;
    }
    private class TVProgramHolder extends RecyclerView.ViewHolder{
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
    private class TVProgramAdapter extends RecyclerView.Adapter<TVProgramHolder>
    {
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

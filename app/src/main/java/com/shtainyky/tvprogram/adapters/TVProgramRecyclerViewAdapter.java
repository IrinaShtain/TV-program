package com.shtainyky.tvprogram.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.model.ProgramItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TVProgramRecyclerViewAdapter extends RecyclerView.Adapter<TVProgramRecyclerViewAdapter.TVProgramRecyclerViewHolder> {
    private List<ProgramItem> mPrograms;
    private Context mContext;

    public TVProgramRecyclerViewAdapter(Context context, List<ProgramItem> programs) {
        mContext = context;
        mPrograms = programs;
    }

    @Override
    public TVProgramRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.adapter_item_program, parent, false);
        return new TVProgramRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TVProgramRecyclerViewHolder holder, int position) {
        ProgramItem program = mPrograms.get(position);
        holder.bindProgram(program);
    }

    @Override
    public int getItemCount() {
        if (mPrograms != null)
            return mPrograms.size();
        else
            return 0;
    }


    static class TVProgramRecyclerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView mTitleTextView;
        @BindView(R.id.time)
        TextView mTimeTextView;
        @BindView(R.id.date)
        TextView mDateTextView;

        TVProgramRecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindProgram(ProgramItem program) {
            mTitleTextView.setText(program.getTitle());
            mTimeTextView.setText(program.getTime());
            mDateTextView.setText(program.getDate());
        }
    }

}
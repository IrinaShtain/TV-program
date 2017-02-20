package com.shtainyky.tvprogram.list_of_programs_displaying.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.model.ProgramItem;

import java.util.List;

public class TVProgramAdapter extends RecyclerView.Adapter<TVProgramHolder> {
    private List<ProgramItem> mPrograms;
    private Context mContext;

    public TVProgramAdapter(Context context, List<ProgramItem> programs) {
        mPrograms = programs;
        mContext = context;
    }

    @Override
    public TVProgramHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_program, parent, false);
        return new TVProgramHolder(view);
    }

    @Override
    public void onBindViewHolder(TVProgramHolder holder, int position) {
        ProgramItem program = mPrograms.get(position);
        holder.bindProgram(program);
    }

    @Override
    public int getItemCount() {
        return mPrograms.size();
    }
}
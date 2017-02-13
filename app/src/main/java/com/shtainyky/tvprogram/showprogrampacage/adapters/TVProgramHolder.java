package com.shtainyky.tvprogram.showprogrampacage.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.model.ProgramItem;

class TVProgramHolder extends RecyclerView.ViewHolder {
    private TextView mTitleTextView;
    private TextView mTimeTextView;
    private TextView mDateTextView;

    TVProgramHolder(View itemView) {
        super(itemView);
        mTitleTextView = (TextView) itemView.findViewById(R.id.title);
        mTimeTextView = (TextView) itemView.findViewById(R.id.time);
        mDateTextView = (TextView) itemView.findViewById(R.id.date);
    }

    void bindProgram(ProgramItem program) {
        mTitleTextView.setText(program.getTitle());
        mTimeTextView.setText(program.getTime());
        mDateTextView.setText(program.getDate());
    }
}

package com.shtainyky.tvprogram.list_of_programs_displaying.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.model.ProgramItem;

import butterknife.BindView;
import butterknife.ButterKnife;

class TVProgramHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.title)
    TextView mTitleTextView;
    @BindView(R.id.time)
    TextView mTimeTextView;
    @BindView(R.id.date)
    TextView mDateTextView;

    TVProgramHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void bindProgram(ProgramItem program) {
        mTitleTextView.setText(program.getTitle());
        mTimeTextView.setText(program.getTime());
        mDateTextView.setText(program.getDate());
    }
}

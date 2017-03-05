package com.shtainyky.tvprogram.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.models.ui.ProgramUI;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TVProgramRecyclerViewAdapter extends CursorRecyclerViewAdapter<TVProgramRecyclerViewAdapter.TVProgramRecyclerViewHolder> {
    private Context mContext;

    public TVProgramRecyclerViewAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
    }

    @Override
    public TVProgramRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.adapter_item_program, parent, false);
        return new TVProgramRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TVProgramRecyclerViewHolder viewHolder, Cursor cursor) {
        cursor.moveToPosition(cursor.getPosition());
        final ProgramUI program = ProgramUI.getProgram(cursor);
        viewHolder.bindProgram(program);
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

        void bindProgram(ProgramUI program) {
            mTitleTextView.setText(program.getTitle());
            mTimeTextView.setText(program.getTime());
            mDateTextView.setText(program.getDate());
        }
    }

}
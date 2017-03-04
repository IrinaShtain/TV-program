package com.shtainyky.tvprogram.adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.models.ui.ChannelUI;
import com.shtainyky.tvprogram.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChannelsRecyclerViewAdapter extends CursorRecyclerViewAdapter<ChannelsRecyclerViewAdapter.ChannelsRecyclerViewHolder> {
    private Context mContext;
    private OnChannelClickListener mOnChannelClickListener;

    public ChannelsRecyclerViewAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;

    }

    public void setOnChannelClickListener(OnChannelClickListener listener) {
        mOnChannelClickListener = listener;
    }

    @Override
    public ChannelsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.adapter_item_channel, parent, false);
        return new ChannelsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChannelsRecyclerViewHolder viewHolder, Cursor cursor) {
        cursor.moveToPosition(cursor.getPosition());
        final ChannelUI channel = ChannelUI.getChannel(cursor);
        viewHolder.bindChannel(channel, mOnChannelClickListener);
    }

    public interface OnChannelClickListener {
        void onChannelClick(ChannelUI channel);
    }

    public static class ChannelsRecyclerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.channel_name)
        TextView mChannelNameTextView;
        @BindView(R.id.channel_category)
        TextView mChannelCategoryTextView;
        @BindView(R.id.channel_logo)
        ImageView mImageView;
        @BindView(R.id.channel_preferred)
        ImageView mImageViewPreferred;

        private boolean mIsChannelPreferred;

        ChannelsRecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindChannel(final ChannelUI channel, final OnChannelClickListener listener) {
            mIsChannelPreferred = channel.getIs_preferred() == 1;
            mChannelNameTextView.setText(channel.getName());
            mChannelCategoryTextView.setText(String.valueOf(channel.getCategory_name()));
            Utils.loadImageFromServerWithPicasso(itemView.getContext(), channel.getPicture(), mImageView);
            setIconForChannel();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onChannelClick(channel);
                }
            });
        }

        private void setIconForChannel() {
            // TODO: 28/02/17 use drawable states to set image
            if (mIsChannelPreferred) {
                setImage(R.drawable.ic_preferred_channel);
            } else {
                setImage(R.drawable.ic_no);
            }
        }

        private void setImage(int drawable) {
            Context context = itemView.getContext();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //>= API 21
                mImageViewPreferred.setImageDrawable(context.getResources().getDrawable(drawable, context.getTheme()));
            } else {
                mImageViewPreferred.setImageDrawable(context.getResources().getDrawable(drawable));
            }
        }
    }
}

package com.shtainyky.tvprogram.listofchannelspacage;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.listofcategoriespacage.ChannelListener;
import com.shtainyky.tvprogram.model.ChannelItem;
import com.shtainyky.tvprogram.parser.Parse;
import com.shtainyky.tvprogram.utils.CheckInternet;

class ListOfChannelsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView mChannelNameTextView;
    private TextView mChannelCategoryTextView;
    private ImageView mImageView;
    private ImageView mImageViewPreferred;
    private String mChannelName;
    private int mChannelId;
    private int mChannelPreferred;
    private int mFromFlag;
    private Context mContext;
    private DatabaseSource mSource;
    private ChannelListener mChannelListener;

    ListOfChannelsHolder(Context context, View itemView, int fromFlag) {
        super(itemView);
        mFromFlag = fromFlag;
        mContext = context;
        mSource = new DatabaseSource(mContext);
        mChannelNameTextView = (TextView) itemView.findViewById(R.id.channel_name);
        mChannelNameTextView.setOnClickListener(this);
        mChannelCategoryTextView = (TextView) itemView.findViewById(R.id.channel_category);
        mChannelCategoryTextView.setOnClickListener(this);
        mImageView = (ImageView) itemView.findViewById(R.id.channel_logo);
        mImageView.setOnClickListener(this);
        mImageViewPreferred = (ImageView) itemView.findViewById(R.id.channel_preferred);
        mImageViewPreferred.setOnClickListener(this);
    }

    void bindChannel(ChannelItem channel) {
        mChannelName = channel.getName();
        mChannelId = channel.getId();
        mChannelPreferred = channel.getIs_preferred();
        mChannelNameTextView.setText(channel.getName());
        mChannelCategoryTextView.setText(String.valueOf(channel.getCategory_name()));

        if (mChannelPreferred == 1) {
            setChannelPreferred(R.drawable.ic_preferred_channel);
        } else {
            setChannelPreferred(R.drawable.ic_no);
        }
        if (!CheckInternet.isOnline(mContext))
            Toast.makeText(mContext, R.string.turn_on_Internet_for_channelImage, Toast.LENGTH_SHORT).show();
        Parse.loadImageFromServerWithPicasso(mContext, channel.getPictureUrl(), mImageView);
    }

    @Override
    public void onClick(View v) {
        String message;
        boolean isPreferred = mChannelPreferred == 1;
        if (!isPreferred)
            message = mContext.getResources().getString(R.string.question_add_preferred, mChannelName);
        else {
            message = mContext.getResources().getString(R.string.question_delete_preferred, mChannelName);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.selecting_title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(mContext.getResources().getString(R.string.answer_no),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(mContext, R.string.cancel, Toast.LENGTH_SHORT).show();
                            }
                        })
                .setPositiveButton(mContext.getResources().getString(R.string.answer_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mChannelPreferred == 1) {
                                    mSource.setChannelPreferred(mChannelId, 0);
                                    Toast.makeText(mContext,
                                            mContext.getResources().getString(R.string.delete_channel_is_preferred, mChannelName),
                                            Toast.LENGTH_LONG).show();
                                    mChannelPreferred = 0;
                                    setChannelPreferred(R.drawable.ic_no);
                                    if (!(mContext instanceof ChannelListener))
                                        throw new AssertionError("Your class must implement ChannelListener");
                                    mChannelListener = (ChannelListener) mContext;
                                    if (mFromFlag == ListOfChannelsFragment.FLAG_PREFERRED)
                                        mChannelListener.setChannelsForCategoryId(0, true);

                                } else {
                                    mSource.setChannelPreferred(mChannelId, 1);
                                    Toast.makeText(mContext,
                                            mContext.getResources().getString(R.string.channel_will_be_preferred, mChannelName),
                                            Toast.LENGTH_LONG).show();
                                    mChannelPreferred = 1;
                                    setChannelPreferred(R.drawable.ic_preferred_channel);
                                }
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void setChannelPreferred(int drawable) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //>= API 21
            mImageViewPreferred.setImageDrawable(mContext.getResources().getDrawable(drawable, mContext.getTheme()));
        } else {
            mImageViewPreferred.setImageDrawable(mContext.getResources().getDrawable(drawable));
        }
    }
}
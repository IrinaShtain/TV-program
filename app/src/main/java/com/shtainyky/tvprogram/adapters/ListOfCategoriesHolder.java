package com.shtainyky.tvprogram.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.adapters.ChannelListener;
import com.shtainyky.tvprogram.model.CategoryItem;
import com.shtainyky.tvprogram.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

// TODO: 20.02.17 holder is used only for ListOfCategoriesAdapter, can be declared inside as public static class
class ListOfCategoriesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.category_name)
     TextView mCategoryNameTextView;
    @BindView(R.id.category_logo)
     ImageView mImageView;
    private int mCategoryId;
    private Context mContext;
    private ChannelListener mChannelListener;

    ListOfCategoriesHolder(Context context, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = context;
        if (!(mContext instanceof ChannelListener))
            throw new AssertionError("Your class must implement ChannelListener");
        mChannelListener = (ChannelListener) mContext;
        itemView.setOnClickListener(this);
    }

    void bindCategory(CategoryItem category) {
        mCategoryNameTextView.setText(category.getTitle());
        // TODO: 20.02.17 don't handle any actions here, you can simply display data
//        and let know about user actions
        if (!Utils.isOnline(mContext)) // you do not need to care about internet here
            Toast.makeText(mContext, R.string.turn_on_Internet_for_categoryImage, Toast.LENGTH_SHORT).show();
        Utils.loadImageFromServerWithPicasso(mContext, category.getImage_url(), mImageView);
        mCategoryId = category.getId();
    }

    @Override
    // TODO: 20.02.17 use ButterKnife
    public void onClick(View v) {
        if (mChannelListener != null) {
            mChannelListener.setChannelsForCategoryId(mCategoryId, false);
        }

    }
}
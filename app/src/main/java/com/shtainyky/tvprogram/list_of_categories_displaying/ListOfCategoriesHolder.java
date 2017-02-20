package com.shtainyky.tvprogram.list_of_categories_displaying;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.model.CategoryItem;
import com.shtainyky.tvprogram.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        if (!Utils.isOnline(mContext))
            Toast.makeText(mContext, R.string.turn_on_Internet_for_categoryImage, Toast.LENGTH_SHORT).show();
        Utils.loadImageFromServerWithPicasso(mContext, category.getImage_url(), mImageView);
        mCategoryId = category.getId();
    }

    @Override
    public void onClick(View v) {
        if (mChannelListener != null) {
            mChannelListener.setChannelsForCategoryId(mCategoryId, false);
        }

    }
}
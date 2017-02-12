package com.shtainyky.tvprogram.listofcategoriespacage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.model.CategoryItem;
import com.shtainyky.tvprogram.parser.Parse;
import com.shtainyky.tvprogram.utils.CheckInternet;

class ListOfCategoriesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView mCategoryNameTextView;
    private ImageView mImageView;
    private int mCategoryId;
    private Context mContext;
    private CategoryListener mCategoryListener;

    ListOfCategoriesHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
        if (!(mContext instanceof CategoryListener))
            throw new AssertionError("Your class must implement CategoryListener");
        mCategoryListener = (CategoryListener) mContext;
        mCategoryNameTextView = (TextView) itemView.findViewById(R.id.category_name);
        mCategoryNameTextView.setOnClickListener(this);
        mImageView = (ImageView) itemView.findViewById(R.id.category_logo);
        mImageView.setOnClickListener(this);
    }

    void bindCategory(CategoryItem category, int position) {
        mCategoryNameTextView.setText(category.getTitle());
        if (!CheckInternet.isOnline(mContext))
            Toast.makeText(mContext, R.string.turn_on_Internet_for_categoryImage, Toast.LENGTH_SHORT).show();
        Parse.loadImageFromServerWithPicasso(mContext, category.getImageUrl(), mImageView);
        mCategoryId = position;
    }

    @Override
    public void onClick(View v) {
        if (mCategoryListener != null) {
            mCategoryListener.setChannelsForCategoryId(mCategoryId);
        }

    }
}
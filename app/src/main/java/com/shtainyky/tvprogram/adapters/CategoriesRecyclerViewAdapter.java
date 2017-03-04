package com.shtainyky.tvprogram.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.models.ui.CategoryUI;
import com.shtainyky.tvprogram.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesRecyclerViewAdapter extends CursorRecyclerViewAdapter<CategoriesRecyclerViewAdapter.CategoriesHolder> {
    private Context mContext;
    private OnCategoryClickListener mOnCategoryClickListener;

    public CategoriesRecyclerViewAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        mOnCategoryClickListener = listener;
    }

    @Override
    public CategoriesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_item_category, parent, false);
        return new CategoriesHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoriesHolder holder, Cursor cursor) {
        cursor.moveToPosition(cursor.getPosition());
        final CategoryUI category = CategoryUI.getCategory(cursor);
        holder.mCategoryNameTextView.setText(category.getTitle());
        Utils.loadImageFromServerWithPicasso(mContext, category.getImage_url(), holder.mImageView);
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCategoryClickListener.onCategoryClick(category.getId());
            }
        });
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(int categoryID);
    }


    static class CategoriesHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.category_name)
        TextView mCategoryNameTextView;
        @BindView(R.id.category_logo)
        ImageView mImageView;
        @BindView(R.id.card_view)
        CardView mCardView;

        CategoriesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}



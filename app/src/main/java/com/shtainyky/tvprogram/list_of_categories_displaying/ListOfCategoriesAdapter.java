package com.shtainyky.tvprogram.list_of_categories_displaying;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.model.CategoryItem;

import java.util.List;

class ListOfCategoriesAdapter extends RecyclerView.Adapter<ListOfCategoriesHolder> {
    private List<CategoryItem> mCategories;
    private Context mContext;
    private OnCategoryClickListener mOnCategoryClickListener;

    ListOfCategoriesAdapter(Context context, List<CategoryItem> categories) {
        mContext = context;
        mCategories = categories;
    }

    // TODO: 20.02.17 use this listener to let know fragment/activity that use this adapter
    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        mOnCategoryClickListener = listener;
    }
    
    @Override
    public ListOfCategoriesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false);
        return new ListOfCategoriesHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(ListOfCategoriesHolder holder, int position) {
        CategoryItem category = mCategories.get(position);
        holder.bindCategory(category);
    }

    @Override
    public int getItemCount() {
        // TODO: 20.02.17 check null?
        return mCategories.size();
    }

    // TODO: 20.02.17 simple inner holder class to bind with category
    
    public interface OnCategoryClickListener {
        void onCategoryClick(int categoryID);
    }
}
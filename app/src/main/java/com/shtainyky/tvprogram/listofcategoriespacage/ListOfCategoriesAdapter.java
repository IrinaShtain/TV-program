package com.shtainyky.tvprogram.listofcategoriespacage;

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

    ListOfCategoriesAdapter(Context context, List<CategoryItem> categories) {
        mContext = context;
        mCategories = categories;
    }

    @Override
    public ListOfCategoriesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.item_category, parent, false);
        return new ListOfCategoriesHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(ListOfCategoriesHolder holder, int position) {
        CategoryItem category = mCategories.get(position);
        holder.bindCategory(category);
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }
}
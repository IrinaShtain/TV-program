package com.shtainyky.tvprogram.navigationdrawerfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.model.Category;

import java.util.ArrayList;
import java.util.List;

public class ListOfCategoriesFragment extends Fragment {
    private RecyclerView mCategoryRecyclerView;
    private ListOfCategoriesAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        mCategoryRecyclerView = (RecyclerView) view
                .findViewById(R.id.categories_recycler_view);
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));

        //will be fixed
        List<Category> categories = new ArrayList<>();
        mAdapter = new ListOfCategoriesAdapter(categories);
        mCategoryRecyclerView.setAdapter(mAdapter);
        return view;
    }


    private class ListOfCategoriesHolder extends RecyclerView.ViewHolder {
        TextView mCategoryNameTextView;
        ImageView mImageView;

        ListOfCategoriesHolder(View itemView) {
            super(itemView);
            mCategoryNameTextView = (TextView) itemView.findViewById(R.id.category_name);
            mImageView = (ImageView) itemView.findViewById(R.id.category_logo);
        }

        public void bindCategory(Category category) {
            mCategoryNameTextView.setText(category.getTitle());
        }
    }

    private class ListOfCategoriesAdapter extends RecyclerView.Adapter<ListOfCategoriesHolder> {
        private List<Category> mCategories;

        public ListOfCategoriesAdapter(List<Category> categories) {
            mCategories = categories;
        }

        @Override
        public ListOfCategoriesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_channel, parent, false);
            return new ListOfCategoriesHolder(view);
        }

        @Override
        public void onBindViewHolder(ListOfCategoriesHolder holder, int position) {
            Category category = mCategories.get(position);
            holder.bindCategory(category);
        }

        @Override
        public int getItemCount() {
            return mCategories.size();
        }
    }

}

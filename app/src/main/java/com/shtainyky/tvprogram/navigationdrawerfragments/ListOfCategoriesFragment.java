package com.shtainyky.tvprogram.navigationdrawerfragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.model.Category;
import com.shtainyky.tvprogram.parser.Parse;
import com.shtainyky.tvprogram.utils.Constants;
import com.shtainyky.tvprogram.utils.QueryPreferences;

import java.util.ArrayList;
import java.util.List;

public class ListOfCategoriesFragment extends Fragment {
    private RecyclerView mCategoryRecyclerView;
    private ListOfCategoriesAdapter mAdapter;
    private List<Category> mCategories = new ArrayList<>();
    private DatabaseSource mSource;
    private ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        mSource = new DatabaseSource(getContext());
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mCategoryRecyclerView = (RecyclerView) view
                .findViewById(R.id.categories_recycler_view);
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));
        requestData();

        return view;
    }

    private void requestData() {
        new MyProgramTask().execute();
    }

    private class MyProgramTask extends AsyncTask<Void, Void, List<Category>> {
        @Override
        protected List<Category> doInBackground(Void... params) {
            if (QueryPreferences.areCategoriesLoaded(getContext())) {
                mCategories = mSource.getAllCategories();
            }
            return mCategories;
        }

        @Override
        protected void onPostExecute(List<Category> categories) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mAdapter = new ListOfCategoriesAdapter(categories);
            mCategoryRecyclerView.setAdapter(mAdapter);
        }
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
            String imageName = Constants.CATEGORY_IMAGE + category.getId() + Constants.PNG;
            Parse.loadImageBitmapFromStorage(getContext(),
                    imageName, mImageView);
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
            View view = layoutInflater.inflate(R.layout.item_category, parent, false);
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

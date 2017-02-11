package com.shtainyky.tvprogram.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.model.CategoryItem;
import com.shtainyky.tvprogram.parser.Parse;
import com.shtainyky.tvprogram.utils.Constants;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import static com.shtainyky.tvprogram.fragments.ListOfChannelsFragment.ARG_CATEGORY_ID;

public class ListOfCategoriesFragment extends Fragment {
    private RecyclerView mCategoryRecyclerView;
    private DatabaseSource mSource;
    private AVLoadingIndicatorView mProgress;
    private List<CategoryItem> mCategories;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        mSource = new DatabaseSource(getContext());
        mCategories = new ArrayList<>();
        mProgress = (AVLoadingIndicatorView) view.findViewById(R.id.progress);
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

    void startAnim() {
        mProgress.show();
    }

    void stopAnim() {
        mProgress.hide();
    }

    private class MyProgramTask extends AsyncTask<Void, Void, List<CategoryItem>> {
        @Override
        protected void onPreExecute() {
            startAnim();
        }

        @Override
        protected List<CategoryItem> doInBackground(Void... params) {
            mCategories = mSource.getAllCategories();
            Log.i("myLog", "MyProgramTask");
            return mCategories;
        }

        @Override
        protected void onPostExecute(List<CategoryItem> categories) {
            stopAnim();
            ListOfCategoriesAdapter mAdapter = new ListOfCategoriesAdapter(categories);
            mCategoryRecyclerView.setAdapter(mAdapter);
        }
    }


    private class ListOfCategoriesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mCategoryNameTextView;
        ImageView mImageView;
        int mCategoryId;

        ListOfCategoriesHolder(View itemView) {
            super(itemView);
            mCategoryNameTextView = (TextView) itemView.findViewById(R.id.category_name);
            mCategoryNameTextView.setOnClickListener(this);
            mImageView = (ImageView) itemView.findViewById(R.id.category_logo);
            mImageView.setOnClickListener(this);
        }

        void bindCategory(CategoryItem category, int position) {
            mCategoryNameTextView.setText(category.getTitle());
            String imageName = Constants.CATEGORY_IMAGE + category.getId() + Constants.PNG;
            Parse.loadImageBitmapFromStorage(getContext(),
                    imageName, mImageView);
            mCategoryId = position;
        }

        @Override
        public void onClick(View v) {
            ListOfChannelsFragment fragment = ListOfChannelsFragment.newInstance(mCategoryId+1);
            FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
            Fragment mFragment = mFragmentManager.findFragmentById(R.id.fragment_container);
            if (mFragment == null) {
                mFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    private class ListOfCategoriesAdapter extends RecyclerView.Adapter<ListOfCategoriesHolder> {
        private List<CategoryItem> mCategories;

        ListOfCategoriesAdapter(List<CategoryItem> categories) {
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
            CategoryItem category = mCategories.get(position);
            holder.bindCategory(category, position);
        }

        @Override
        public int getItemCount() {
            return mCategories.size();
        }
    }

}

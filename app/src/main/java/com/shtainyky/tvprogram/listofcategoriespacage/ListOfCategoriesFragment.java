package com.shtainyky.tvprogram.listofcategoriespacage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.model.CategoryItem;

import java.util.ArrayList;
import java.util.List;

public class ListOfCategoriesFragment extends Fragment {
    private RecyclerView mCategoryRecyclerView;
    private DatabaseSource mSource;
    private List<CategoryItem> mCategories;

    public static ListOfCategoriesFragment newInstance() {

        return new ListOfCategoriesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        mSource = new DatabaseSource(getContext());
        mCategories = new ArrayList<>();
        mCategoryRecyclerView = (RecyclerView) view
                .findViewById(R.id.categories_recycler_view);
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));
        requestData();

        return view;
    }

    private void requestData() {
        mCategories = mSource.getAllCategories();
        ListOfCategoriesAdapter mAdapter = new ListOfCategoriesAdapter(getContext(), mCategories);
        mCategoryRecyclerView.setAdapter(mAdapter);
    }


}

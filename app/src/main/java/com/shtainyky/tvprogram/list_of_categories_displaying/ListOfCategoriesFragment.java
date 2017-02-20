package com.shtainyky.tvprogram.list_of_categories_displaying;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListOfCategoriesFragment extends Fragment {
    @BindView(R.id.categories_recycler_view)
    RecyclerView mCategoryRecyclerView;
    private DatabaseSource mSource;
    private List<CategoryItem> mCategories;

    public static ListOfCategoriesFragment newInstance() {

        return new ListOfCategoriesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        ButterKnife.bind(this, view);
        mSource = new DatabaseSource(getContext());
        mCategories = new ArrayList<>();

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

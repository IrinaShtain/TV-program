package com.shtainyky.tvprogram.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.adapters.CategoriesRecyclerViewAdapter;
import com.shtainyky.tvprogram.database.ContractClass;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListOfCategoriesFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static int LOADER_ID = 0;
    // TODO: 28/02/17  CategoriesRecyclerViewAdapter.OnCategoryClickListener and ListOfCategoriesFragment.<InteractionListener> not the same

    private CategoriesRecyclerViewAdapter.OnCategoryClickListener mCallback;
    @BindView(R.id.categories_recycler_view)
    RecyclerView mCategoryRecyclerView;
    @BindView(R.id.progress)
    AVLoadingIndicatorView mProgress;

    public static ListOfCategoriesFragment newInstance() {

        return new ListOfCategoriesFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = null;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
        try {
            mCallback = (CategoriesRecyclerViewAdapter.OnCategoryClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCategoryClickListener");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        ButterKnife.bind(this, view);
        setupAdapterForRecycleView();
        // TODO: 20.02.17 show data loading progress
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestData();
    }

    // TODO: 20.02.17 request data ASYNC using content provider
    private void requestData() {
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void setupAdapterForRecycleView() {
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        CategoriesRecyclerViewAdapter adapter = new CategoriesRecyclerViewAdapter(getContext(), null);
        adapter.setOnCategoryClickListener(mCallback);
        mCategoryRecyclerView.setAdapter(adapter);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i("myLog", "onCreateLoader =");
        mProgress.show();
        Uri CONTACT_URI = Uri.parse(String.valueOf(ContractClass.Categories.CONTENT_URI));
        return new CursorLoader(getContext(), CONTACT_URI, ContractClass.Categories.DEFAULT_PROJECTION,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        ((CategoriesRecyclerViewAdapter) mCategoryRecyclerView.getAdapter()).swapCursor(data);
        Log.i("myLog", "onLoadFinished =");
        mProgress.hide();
    }

    @Override
    public void onLoaderReset(Loader loader) {
        ((CategoriesRecyclerViewAdapter) mCategoryRecyclerView.getAdapter()).swapCursor(null);
    }
}

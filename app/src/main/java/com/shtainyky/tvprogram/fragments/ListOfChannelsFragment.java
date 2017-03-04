package com.shtainyky.tvprogram.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.adapters.ChannelsRecyclerViewAdapter;
import com.shtainyky.tvprogram.adapters.CursorRecyclerViewAdapter;
import com.shtainyky.tvprogram.database.ContractClass;
import com.shtainyky.tvprogram.database.DatabaseStoreImp;
import com.shtainyky.tvprogram.models.models_ui.ChannelUI;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shtainyky.tvprogram.database.ContractClass.Channels.COLUMN_CHANNEL_CATEGORY_ID;
import static com.shtainyky.tvprogram.database.ContractClass.Channels.COLUMN_CHANNEL_IS_PREFERRED;
import static com.shtainyky.tvprogram.database.ContractClass.Channels.COLUMN_CHANNEL_TITLE;

public class ListOfChannelsFragment extends Fragment implements ChannelsRecyclerViewAdapter.OnChannelClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {
    public static final String ARG_CATEGORY_ID = "mCategoryId";
    public static final String ARG_IS_PREFERRED = "is_preferred";

    private DatabaseStoreImp mSource;
    private int mCategoryId;
    private boolean mIsPreferred;
    private OnChannelClickListener mOnChannelClickListener;
    private OnPreferredChannelClickListener mOnPreferredChannelClickListener;

    @BindView(R.id.list_of_channels_recycler_view)
    RecyclerView mChannelsRecyclerView;
    @BindView(R.id.buttonPreferred)
    Button buttonPreferred;

    public static ListOfChannelsFragment newInstance(int categoryId, boolean isPreferred) {
        ListOfChannelsFragment fragment = new ListOfChannelsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_CATEGORY_ID, categoryId);
        bundle.putBoolean(ARG_IS_PREFERRED, isPreferred);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = null;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
        try {
            mOnChannelClickListener = (OnChannelClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnChannelClickListener");
        }
        try {
            mOnPreferredChannelClickListener = (OnPreferredChannelClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnPreferredChannelClickListener");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_of_channels, container, false);
        ButterKnife.bind(this, view);

        mSource = new DatabaseStoreImp(getContext());
        getBundle();
        setupAdapter();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupAndInitLoader(2);
    }

    private void getBundle() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mCategoryId = bundle.getInt(ARG_CATEGORY_ID);
            mIsPreferred = bundle.getBoolean(ARG_IS_PREFERRED);
        }
    }

    private void setupAdapter() {
        mChannelsRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));
        ChannelsRecyclerViewAdapter adapter = new ChannelsRecyclerViewAdapter(getContext(), null);
        adapter.setOnChannelClickListener(this);
        mChannelsRecyclerView.setAdapter(adapter);


    }

    private void setupAndInitLoader(int loaderId) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_CATEGORY_ID, mCategoryId);
        bundle.putBoolean(ARG_IS_PREFERRED, mIsPreferred);
        getLoaderManager().initLoader(loaderId, bundle, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri CONTACT_URI = ContractClass.Channels.CONTENT_URI;
        int categoryId = args.getInt(ARG_CATEGORY_ID, 0);
        boolean isPreferred = args.getBoolean(ARG_IS_PREFERRED, false);
        if (isPreferred)
            return new CursorLoader(getContext(), CONTACT_URI,
                    ContractClass.Channels.DEFAULT_PROJECTION,
                    COLUMN_CHANNEL_IS_PREFERRED + "=?",
                    new String[]{"" + 1},
                    COLUMN_CHANNEL_TITLE + " ASC ");
        else if (categoryId == 0)
            return new CursorLoader(getContext(), CONTACT_URI,
                    ContractClass.Channels.DEFAULT_PROJECTION,
                    null,
                    null,
                    COLUMN_CHANNEL_TITLE + " ASC ");
        else
            return new CursorLoader(getContext(), CONTACT_URI,
                    ContractClass.Channels.DEFAULT_PROJECTION,
                    COLUMN_CHANNEL_CATEGORY_ID + "=?",
                    new String[]{String.valueOf(categoryId)},
                    COLUMN_CHANNEL_TITLE + " ASC ");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mIsPreferred) {
            buttonPreferred.setVisibility(View.VISIBLE);
            if (data.getCount() != 0) {
                buttonPreferred.setText(R.string.show_preferred);
                buttonPreferred.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnPreferredChannelClickListener.onPreferredChannelClick();
                    }
                });
            } else {
                buttonPreferred.setText(R.string.show_nothing);
                buttonPreferred.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnChannelClickListener.onChannelClick();
                    }
                });
            }
        }
        ((CursorRecyclerViewAdapter) mChannelsRecyclerView.getAdapter()).swapCursor(data);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((ChannelsRecyclerViewAdapter) mChannelsRecyclerView.getAdapter()).swapCursor(null);
    }

    @Override
    public void onChannelClick(ChannelUI channel) {
        showAlertDialog(channel);
    }

    private void showAlertDialog(final ChannelUI channelItem) {
        final boolean isPreferred = channelItem.getIs_preferred() == 1;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.selecting_title)
                .setMessage(getQuestionForAlertDialog(isPreferred, channelItem.getName()))
                .setCancelable(false)
                .setNegativeButton(getContext().getResources().getString(R.string.answer_no), null)
                .setPositiveButton(getContext().getResources().getString(R.string.answer_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (isPreferred) {
                                    setChannelNotPreferred(channelItem);
                                } else {
                                    setChannelPreferred(channelItem);
                                }
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void setChannelNotPreferred(ChannelUI channelUI) {
        mSource.setChannelPreferred(channelUI.getId(), 0);
        Toast.makeText(getContext(),
                getContext().getResources().getString(R.string.delete_channel_is_preferred, channelUI.getName()),
                Toast.LENGTH_LONG).show();
    }

    private void setChannelPreferred(ChannelUI channelUI) {
        mSource.setChannelPreferred(channelUI.getId(), 1);
        Toast.makeText(getContext(),
                getContext().getResources().getString(R.string.channel_will_be_preferred, channelUI.getName()),
                Toast.LENGTH_LONG).show();
    }

    private String getQuestionForAlertDialog(boolean isPreferred, String channelName) {
        if (!isPreferred)
            return getContext().getResources().getString(R.string.question_add_preferred, channelName);
        else {
            return getContext().getResources().getString(R.string.question_delete_preferred, channelName);
        }
    }

    public ListOfChannelsFragment() {
        super();
    }


    public interface OnPreferredChannelClickListener {
        void onPreferredChannelClick();
    }

    public interface OnChannelClickListener {
        void onChannelClick();
    }


}

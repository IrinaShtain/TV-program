package com.shtainyky.tvprogram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.httpconnection.HttpManager;
import com.shtainyky.tvprogram.model.CategoryItem;
import com.shtainyky.tvprogram.model.ChannelItem;
import com.shtainyky.tvprogram.model.ProgramItem;
import com.shtainyky.tvprogram.parser.Parse;
import com.shtainyky.tvprogram.service.LoadingMonthProgramsIntentService;
import com.shtainyky.tvprogram.utils.CheckInternet;
import com.shtainyky.tvprogram.utils.Constants;
import com.shtainyky.tvprogram.utils.QueryPreferences;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Calendar;
import java.util.List;

public class LoadingDataActivity extends AppCompatActivity {
    private final static String TAG = "myLog";
    private DatabaseSource mSource;
    private AVLoadingIndicatorView mCustomProgresBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadind_data);
        setupToolbarMenu();
        mCustomProgresBar = (AVLoadingIndicatorView) findViewById(R.id.avi);
        mSource = new DatabaseSource(this);
        if (CheckInternet.isOnline(this)) {
            new MyLoadingChannelsTask().execute();
            new MyLoadingCategoriesTask().execute();
            new MyLoadingProgram().execute();

        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.no_internet,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void setupToolbarMenu() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.loading_data);
    }

    private void finishLoading() {
        Toast.makeText(getApplicationContext(), R.string.data_are_loaded, Toast.LENGTH_SHORT).show();
        QueryPreferences.setStoredFirstInstallation(getApplicationContext(), false);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

        finish();
    }

    private class MyLoadingCategoriesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackgroundMyLoadingCategoriesTask");
            //loading categories
            String contentCategories = HttpManager.getData(Constants.URI_CATEGORIES);
            List<CategoryItem> categories = Parse.parseJSONtoListCategory(contentCategories);
            mSource.insertListCategories(categories);
            QueryPreferences.setCategoryLoaded(getApplicationContext(), true);
            Log.i(TAG, "CATEGORIESdoInBackgroundEnd");

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(), R.string.categories_are_loaded, Toast.LENGTH_SHORT).show();
            if (QueryPreferences.areChannelsLoaded(getApplicationContext()) && QueryPreferences.areProgramLoaded(getApplicationContext()))
                finishLoading();
        }
    }


    private class MyLoadingChannelsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackgroundMyLoadingChannelsTask");
            //loading channels
            String contentChannels = HttpManager.getData(Constants.URI_CHANNELS);
            List<ChannelItem> channels = Parse.parseJSONtoListChannels(contentChannels);
            mSource.insertListChannels(channels);
            QueryPreferences.setChannelLoaded(getApplicationContext(), true);
            Log.i(TAG, "MyLoadingChannelsTaskdoInBackgroundEnd");

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(), R.string.channels_are_loaded, Toast.LENGTH_SHORT).show();
            if (QueryPreferences.areCategoriesLoaded(getApplicationContext()) && QueryPreferences.areProgramLoaded(getApplicationContext()))
                finishLoading();
        }
    }


    private class MyLoadingProgram extends AsyncTask<Void, Integer, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < 1; i++) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, i);
                long timeStamp = calendar.getTimeInMillis();
                String response = HttpManager.getData(Constants.URI_PROGRAMS + timeStamp);
                List<ProgramItem> programs = Parse.parseJSONtoListPrograms(response);
                mSource.insertListPrograms(programs);
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            QueryPreferences.setCountLoadedDays(getApplicationContext(), 7);
            Toast.makeText(getApplicationContext(), R.string.programs_are_loaded, Toast.LENGTH_SHORT).show();
            LoadingMonthProgramsIntentService.startService(getApplicationContext(), true);
            if (QueryPreferences.areCategoriesLoaded(getApplicationContext()) && QueryPreferences.areChannelsLoaded(getApplicationContext()))
                finishLoading();
        }
    }
}

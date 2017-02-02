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
import com.shtainyky.tvprogram.model.Category;
import com.shtainyky.tvprogram.model.Channel;
import com.shtainyky.tvprogram.model.Program;
import com.shtainyky.tvprogram.parser.Parse;
import com.shtainyky.tvprogram.utils.Constants;
import com.shtainyky.tvprogram.httpconnection.HttpManager;
import com.shtainyky.tvprogram.utils.QueryPreferences;
import com.shtainyky.tvprogram.utils.CheckInternet;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Calendar;
import java.util.List;

public class LoadingDataActivity extends AppCompatActivity {
    private final static String TAG = "myLog";
    private DatabaseSource mSource;
    private AVLoadingIndicatorView mCustomProgresBar;
    private ProgressBar mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadind_data);
        setupToolbarMenu();
        mCustomProgresBar = (AVLoadingIndicatorView) findViewById(R.id.avi);
        mProgress = (ProgressBar) findViewById(R.id.progress_bar);
        mProgress.setProgress(5);

        mSource = new DatabaseSource(this);
        if (CheckInternet.isOnline(this)) {
            new MyLoadingChannelsTask().execute();
            new MyLoadingCategoriesTask().execute();
            new MyLoadingProgramForLongPeriodTask().execute();

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
            List<Category> categories = Parse.parseJSONtoListCategory(contentCategories);
            if (categories != null) {
                for (int i = 0; i < categories.size(); i++) {
                    String url = categories.get(i).getImageUrl();
                    String imageName = Constants.CATEGORY_IMAGE
                            + categories.get(i).getId()
                            + Constants.PNG;
                    Bitmap bitmap = Parse.loadImageFromServer(url);
                    Parse.saveImageToStorage(getApplicationContext(), bitmap, imageName);
                }
            }
            mSource.insertListCategories(categories);
            QueryPreferences.setCategoryLoaded(getApplicationContext(), true);
            Log.i(TAG, "CATEGORIESdoInBackgroundEnd");

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgress.setProgress(30);
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
            List<Channel> channels = Parse.parseJSONtoListChannels(contentChannels);
            if (channels != null) {
                for (int i = 0; i < channels.size(); i++) {
                    String url = channels.get(i).getPictureUrl();
                    String imageName = Constants.CHANNEL_IMAGE
                            + channels.get(i).getId()
                            + Constants.PNG;
                    Bitmap bitmap = Parse.loadImageFromServer(url);
                    Parse.saveImageToStorage(getApplicationContext(), bitmap, imageName);
                }
            }
            mSource.insertListChannels(channels);
            QueryPreferences.setChannelLoaded(getApplicationContext(), true);
            Log.i(TAG, "MyLoadingChannelsTaskdoInBackgroundEnd");

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgress.setProgress(15);
            Toast.makeText(getApplicationContext(), R.string.channels_are_loaded, Toast.LENGTH_SHORT).show();
            if (QueryPreferences.areCategoriesLoaded(getApplicationContext()) && QueryPreferences.areProgramLoaded(getApplicationContext()))
                finishLoading();
        }
    }


    private class MyLoadingProgramForLongPeriodTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {

            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.data_are_loading_for_seven_day),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < 8; i++) {
                Calendar calendar = Calendar.getInstance();
                DatabaseSource mSource = new DatabaseSource(getApplicationContext());
                calendar.add(Calendar.DAY_OF_YEAR, i);
                long timeStamp = calendar.getTimeInMillis();
                String response = HttpManager.getData(Constants.URI_PROGRAMS + timeStamp);
                List<Program> programs = Parse.parseJSONtoListPrograms(response);
                mSource.insertListPrograms(programs);
                publishProgress(i);
                Log.i(TAG, "HttpManager " + i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgress.setProgress(30 + values[0]*10);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            QueryPreferences.setCountLoadedDays(getApplicationContext(), 7);
            Toast.makeText(getApplicationContext(), R.string.programs_are_loaded, Toast.LENGTH_SHORT).show();
            if (QueryPreferences.areCategoriesLoaded(getApplicationContext()) && QueryPreferences.areChannelsLoaded(getApplicationContext()))
                finishLoading();
        }
    }
}

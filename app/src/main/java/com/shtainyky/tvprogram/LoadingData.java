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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.model.Category;
import com.shtainyky.tvprogram.model.Channel;
import com.shtainyky.tvprogram.model.Program;
import com.shtainyky.tvprogram.parser.Parse;
import com.shtainyky.tvprogram.service.MyIntentService;
import com.shtainyky.tvprogram.service.NotificationAboutLoading;
import com.shtainyky.tvprogram.utils.Constants;
import com.shtainyky.tvprogram.utils.HttpManager;
import com.shtainyky.tvprogram.utils.QueryPreferences;
import com.shtainyky.tvprogram.utils.SomeMethods;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;

public class LoadingData extends AppCompatActivity {
    private final static String TAG = "myLog";
    private DatabaseSource mSource;
    private AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadind_data);
        setupToolbarMenu();
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
       // ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mSource = new DatabaseSource(this);
        if (SomeMethods.isOnline(this)) {
            new MyLoadingChannelsTask().execute();
            new MyLoadingCategoriesTask().execute();
            new MyLoadingProgramTask().execute();
            new MyLoadingProgramForLongPeriodTask().execute();

        } else {
            Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_LONG).show();
        }
    }

    private void setupToolbarMenu() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.loading_data);
    }

    private void finishLoading() {
        Toast.makeText(getApplicationContext(), R.string.data_are_loaded, Toast.LENGTH_SHORT).show();
        QueryPreferences.setStoredFirstInstallation(getApplicationContext(), false);
//
//        Intent i = new Intent(this, MyIntentService.class);
//        startService(i);

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

            Toast.makeText(getApplicationContext(), R.string.channels_are_loaded, Toast.LENGTH_SHORT).show();
            if (QueryPreferences.areCategoriesLoaded(getApplicationContext()) && QueryPreferences.areProgramLoaded(getApplicationContext()))
                finishLoading();
        }
    }

    private class MyLoadingProgramTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "MyLoadingProgramTaskdoInBackgroundStart");
            String response = HttpManager.getData(Constants.URI_PROGRAMS + Calendar.getInstance().getTimeInMillis());
            List<Program> programs = Parse.parseJSONtoListPrograms(response);
            mSource.insertListPrograms(programs);
            QueryPreferences.setProgramLoaded(getApplicationContext(), true);
            Log.i(TAG, "MyLoadingProgramTaskdoInBackgroundEnd");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(), R.string.programs_are_loaded, Toast.LENGTH_SHORT).show();
            if (QueryPreferences.areCategoriesLoaded(getApplicationContext()) && QueryPreferences.areChannelsLoaded(getApplicationContext()))
                finishLoading();
        }
    }

    private class MyLoadingProgramForLongPeriodTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), R.string.data_are_loading_for_a_month, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Calendar calendar = Calendar.getInstance();
            final DatabaseSource mSource = new DatabaseSource(getApplicationContext());
            for (int i = 1; i < 11; i++) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                long timeStamp = calendar.getTimeInMillis();
                final int finalI = i;
                StringRequest request = new StringRequest(Constants.URI_PROGRAMS + timeStamp,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String utfString = "";
                                try {
                                    utfString = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                                    List<Program> programs = Parse.parseJSONtoListPrograms(utfString);
                                    if (programs != null) {
                                        mSource.insertListPrograms(programs);
                                        Log.i(TAG, "UnsupportedEncodingException " + finalI);

                                    } else {
                                        Log.i(TAG, "NUULL " + utfString);
                                        Log.i(TAG, "NUULLresponse " + response);
                                    }
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                    Log.i(TAG, "UnsupportedEncodingException ");
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(request);
            }

            //NotificationAboutLoading.sendNotification(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(), R.string.data_are_loading_for_a_month, Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), R.string.programs_are_loaded, Toast.LENGTH_SHORT).show();
            QueryPreferences.setIsServiceWorking(getApplicationContext(), false);
            if (QueryPreferences.areCategoriesLoaded(getApplicationContext()) && QueryPreferences.areChannelsLoaded(getApplicationContext()))
                finishLoading();
        }
    }
}

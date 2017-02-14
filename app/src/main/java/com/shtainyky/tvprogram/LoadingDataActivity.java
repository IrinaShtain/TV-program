package com.shtainyky.tvprogram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.httpconnection.HttpManager;
import com.shtainyky.tvprogram.model.CategoryItem;
import com.shtainyky.tvprogram.model.ChannelItem;
import com.shtainyky.tvprogram.model.ProgramItem;
import com.shtainyky.tvprogram.utils.Utils;
import com.shtainyky.tvprogram.utils.QueryPreferences;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingDataActivity extends AppCompatActivity {
    private final static String TAG = "myLog";
    private DatabaseSource mSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadind_data);
        setupToolbarMenu();
        mSource = new DatabaseSource(this);
        if (Utils.isOnline(this)) {
            loadChannels();
            loadCategories();
            loadPrograms();

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

//        Intent intents = new Intent(getApplicationContext(), LoadingMonthProgramsIntentService.class);
//        startService(intents);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

        finish();
    }

    private void loadChannels() {
        Call<List<ChannelItem>> callChannels = HttpManager.getApiService().getChannelsList();
        callChannels.enqueue(new Callback<List<ChannelItem>>() {
            @Override
            public void onResponse(Call<List<ChannelItem>> call, Response<List<ChannelItem>> response) {
                mSource.insertListChannels(response.body());
                QueryPreferences.setChannelLoaded(getApplicationContext(), true);
                if (QueryPreferences.areCategoriesLoaded(getApplicationContext()) && QueryPreferences.areProgramLoaded(getApplicationContext()))
                    finishLoading();
                Log.i(TAG, "ChannelsOnResponse");
            }

            @Override
            public void onFailure(Call<List<ChannelItem>> call, Throwable t) {
                Log.i(TAG, "ChannelsOnFailure");
            }
        });
    }

    private void loadCategories() {
        Call<List<CategoryItem>> callCategories = HttpManager.getApiService().getCategoriesList();
        callCategories.enqueue(new Callback<List<CategoryItem>>() {
            @Override
            public void onResponse(Call<List<CategoryItem>> call, Response<List<CategoryItem>> response) {
                mSource.insertListCategories(response.body());
                QueryPreferences.setCategoryLoaded(getApplicationContext(), true);
                if (QueryPreferences.areProgramLoaded(getApplicationContext()) && QueryPreferences.areChannelsLoaded(getApplicationContext()))
                    finishLoading();
                Log.i(TAG, "CATEGORIESonResponse");
            }

            @Override
            public void onFailure(Call<List<CategoryItem>> call, Throwable t) {
                Log.i(TAG, "CATEGORIESonFailure");
            }
        });
    }

    private void loadPrograms() {
        Calendar calendar = Calendar.getInstance();
        long timeStamp = calendar.getTimeInMillis();
        Call<List<ProgramItem>> callPrograms = HttpManager.getApiService().getProgramsList(timeStamp);
        callPrograms.enqueue(new Callback<List<ProgramItem>>() {
            @Override
            public void onResponse(Call<List<ProgramItem>> call, Response<List<ProgramItem>> response) {
                mSource.insertListPrograms(response.body());
                QueryPreferences.setCountLoadedDays(getApplicationContext(), 1);
                if (QueryPreferences.areCategoriesLoaded(getApplicationContext()) && QueryPreferences.areChannelsLoaded(getApplicationContext()))
                    finishLoading();
                Log.i(TAG, "ProgramItemonResponse");
            }

            @Override
            public void onFailure(Call<List<ProgramItem>> call, Throwable t) {
                Log.i(TAG, "ProgramItemonFailure");
            }
        });

    }
}

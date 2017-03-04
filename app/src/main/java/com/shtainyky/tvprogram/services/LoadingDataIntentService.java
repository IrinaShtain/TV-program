package com.shtainyky.tvprogram.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.activities.LoadingDataActivity;
import com.shtainyky.tvprogram.database.DatabaseStoreImp;
import com.shtainyky.tvprogram.models.models_retrofit.Category;
import com.shtainyky.tvprogram.models.models_retrofit.Channel;
import com.shtainyky.tvprogram.models.models_retrofit.Program;
import com.shtainyky.tvprogram.services.httpconnection.HttpManager;
import com.shtainyky.tvprogram.model.CategoryItem;
import com.shtainyky.tvprogram.model.ChannelItem;
import com.shtainyky.tvprogram.model.ProgramItem;
import com.shtainyky.tvprogram.utils.QueryPreferences;
import com.shtainyky.tvprogram.utils.Utils;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoadingDataIntentService extends IntentService {
    private final static String TAG = "myLog";
    private DatabaseStoreImp mSource;

    public LoadingDataIntentService() {
        super("LoadingDataIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (Utils.isOnline(this)) {
            Log.i(TAG, "LoadingDataIntentService");
            mSource = new DatabaseStoreImp(getApplicationContext());
            loadCategories();
            loadPrograms();
            loadChannels();


        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.no_internet,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void loadChannels() {
        Call<List<Channel>> callChannels = HttpManager.getApiService().getChannelsList();
        callChannels.enqueue(new Callback<List<Channel>>() {
            @Override
            public void onResponse(Call<List<Channel>> call, Response<List<Channel>> response) {
                if (response.body() == null) {
                    showMessageToUser();
                    return;
                }
                mSource.insertListChannels(response.body());
                if (QueryPreferences.areCategoriesLoaded(getApplicationContext())
                        && QueryPreferences.areProgramsLoaded(getApplicationContext()))
                    sendResult();
                Log.i(TAG, "ChannelsOnResponse");
            }

            @Override
            public void onFailure(Call<List<Channel>> call, Throwable t) {
                Log.i(TAG, "ChannelsOnFailure");
            }
        });
    }

    private void loadCategories() {
        Call<List<Category>> callCategories = HttpManager.getApiService().getCategoriesList();
        callCategories.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.body() == null) {
                    showMessageToUser();
                    return;
                }
                mSource.insertListCategories(response.body());
                if (QueryPreferences.areProgramsLoaded(getApplicationContext())
                        && QueryPreferences.areChannelsLoaded(getApplicationContext()))
                    sendResult();
                Log.i(TAG, "CATEGORIESonResponse");
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.i(TAG, "CATEGORIESonFailure");
            }
        });
    }

    private void loadPrograms() {
        Calendar calendar = Calendar.getInstance();
        long timeStamp = calendar.getTimeInMillis();
        Call<List<Program>> callPrograms = HttpManager.getApiService().getProgramsList(timeStamp);
        callPrograms.enqueue(new Callback<List<Program>>() {
            @Override
            public void onResponse(Call<List<Program>> call, Response<List<Program>> response) {
                if (response.body() == null) {
                    showMessageToUser();
                    return;
                }
                mSource.insertListPrograms(response.body());
                QueryPreferences.setCountLoadedDays(getApplicationContext(), 1);
                if (QueryPreferences.areCategoriesLoaded(getApplicationContext())
                        && QueryPreferences.areChannelsLoaded(getApplicationContext()))
                    sendResult();
                Log.i(TAG, "ProgramItemonResponse");
            }

            @Override
            public void onFailure(Call<List<Program>> call, Throwable t) {
                Log.i(TAG, "ProgramItemonFailure");
            }
        });

    }

    private void showMessageToUser() {
        Toast.makeText(this, "Server is not available now. Please, try again later", Toast.LENGTH_SHORT).show();
    }

    private void sendResult() {
        Intent intent = new Intent(LoadingDataActivity.BROADCAST_ACTION);
        intent.putExtra(LoadingDataActivity.PARAM_STATUS, LoadingDataActivity.STATUS_FINISH);
        Log.i(TAG, "MyIntentServicesendResult");
        sendBroadcast(intent);
    }


}

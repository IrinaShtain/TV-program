package com.shtainyky.tvprogram.loading_data;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.httpconnection.HttpManager;
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
    private DatabaseSource mSource;

    public LoadingDataIntentService() {
        super("LoadingDataIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (Utils.isOnline(this)) {
            Log.i(TAG, "LoadingDataIntentService");
            mSource = new DatabaseSource(getApplicationContext());
            loadChannels();
            loadCategories();
            loadPrograms();

        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.no_internet,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void loadChannels() {
        Call<List<ChannelItem>> callChannels = HttpManager.getApiService().getChannelsList();
        callChannels.enqueue(new Callback<List<ChannelItem>>() {
            @Override
            public void onResponse(Call<List<ChannelItem>> call, Response<List<ChannelItem>> response) {
                if (response.body() == null) {
                    showMessageToUser();
                    return;
                }
                mSource.insertListChannels(response.body());
                if (QueryPreferences.areCategoriesLoaded(getApplicationContext())
                        && QueryPreferences.areProgramLoaded(getApplicationContext()))
                    sendResult();
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
                if (response.body() == null) {
                    showMessageToUser();
                    return;
                }
                mSource.insertListCategories(response.body());
                if (QueryPreferences.areProgramLoaded(getApplicationContext())
                        && QueryPreferences.areChannelsLoaded(getApplicationContext()))
                    sendResult();
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
            public void onFailure(Call<List<ProgramItem>> call, Throwable t) {
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

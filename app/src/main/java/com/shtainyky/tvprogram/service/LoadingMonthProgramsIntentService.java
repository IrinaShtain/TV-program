package com.shtainyky.tvprogram.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.model.ProgramItem;
import com.shtainyky.tvprogram.parser.Parse;
import com.shtainyky.tvprogram.utils.Constants;
import com.shtainyky.tvprogram.httpconnection.HttpManager;
import com.shtainyky.tvprogram.utils.QueryPreferences;
import com.shtainyky.tvprogram.utils.CheckInternet;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoadingMonthProgramsIntentService extends IntentService {

    public LoadingMonthProgramsIntentService() {
        super("LoadingMonthProgramsIntentService");
    }


    public static Intent newIntent(Context context) {
        return new Intent(context, LoadingMonthProgramsIntentService.class);
    }

    public static void startService(Context context, boolean isOn) {
        Intent intent = LoadingMonthProgramsIntentService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_DAY, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
        Log.i("myLog", "startService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("myLog", "will startService");
        if (!CheckInternet.isOnline(getApplicationContext())) return;
        loadPrograms();
        NotificationAboutLoading.sendNotification(getApplicationContext(),
                getResources().getString(R.string.data_are_loaded_for_period,
                        String.valueOf(30)), 0);
        Log.i("myLog", "HttpManager worked");
    }

    private void loadPrograms() {
        final DatabaseSource source = new DatabaseSource(getApplicationContext());
        for (int i = 1; i < 31; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, i);
            long timeStamp = calendar.getTimeInMillis();
            Call<List<ProgramItem>> callPrograms = HttpManager.getApiService().getProgramsList(timeStamp);
            final int finalI = i;
            callPrograms.enqueue(new Callback<List<ProgramItem>>() {
                @Override
                public void onResponse(Call<List<ProgramItem>> call, Response<List<ProgramItem>> response) {
                    source.insertListPrograms(response.body());
                    QueryPreferences.setCountLoadedDays(getApplicationContext(), finalI);

                    Log.i("myLog", "ProgramItemonResponse Service");
                }

                @Override
                public void onFailure(Call<List<ProgramItem>> call, Throwable t) {
                    Log.i("myLog", "ProgramItemonFailureService");
                }
            });
        }
    }


}

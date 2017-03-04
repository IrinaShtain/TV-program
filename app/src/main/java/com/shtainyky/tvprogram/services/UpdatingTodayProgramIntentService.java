package com.shtainyky.tvprogram.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.database.DatabaseStoreImp;
import com.shtainyky.tvprogram.models.retrofit.Program;
import com.shtainyky.tvprogram.services.httpconnection.HttpManager;
import com.shtainyky.tvprogram.utils.NotificationAboutLoading;
import com.shtainyky.tvprogram.utils.Utils;
import com.shtainyky.tvprogram.utils.QueryPreferences;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UpdatingTodayProgramIntentService extends IntentService {
    public static final String TAG = "myLog";

    public UpdatingTodayProgramIntentService() {
        super(TAG);

    }
    public static Intent newIntent(Context context) {
        return new Intent(context, UpdatingTodayProgramIntentService.class);
    }

    public static void setServiceAlarm(Context context) {
        Intent i = UpdatingTodayProgramIntentService.newIntent(context);
        boolean isOn = QueryPreferences.isUpdatingDayProgramOn(context);
        Log.i(TAG, "Received an intent:setServiceAlarm " + isOn);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_HOUR * 6, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);
        if (!Utils.isOnline(getApplicationContext())) return;
        final DatabaseStoreImp source = new DatabaseStoreImp(getApplicationContext());
        Calendar calendar = Calendar.getInstance();
        long timeStamp = calendar.getTimeInMillis();
        Call<List<Program>> callPrograms = HttpManager.getApiService().getProgramsList(timeStamp);
        callPrograms.enqueue(new Callback<List<Program>>() {
            @Override
            public void onResponse(Call<List<Program>> call, Response<List<Program>> response) {
                source.updateListPrograms(response.body(), String.valueOf(DateFormat.format("dd/MM/yyyy", Calendar.getInstance())));
                NotificationAboutLoading.sendNotification(getApplicationContext(), getString(R.string.data_updated), 1);
                Log.i(TAG, "ProgramItemononHandleIntentResponse");
            }

            @Override
            public void onFailure(Call<List<Program>> call, Throwable t) {
                Log.i(TAG, "ProgramItemonFailure");
            }
        });


    }
}




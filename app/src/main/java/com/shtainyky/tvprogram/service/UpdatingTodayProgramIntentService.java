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
import com.shtainyky.tvprogram.httpconnection.HttpManager;
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
        boolean isOn = QueryPreferences.getShouldUpdateDayProgram(context);
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
        final DatabaseSource source = new DatabaseSource(getApplicationContext());
        DatabaseSource mSource = new DatabaseSource(getApplicationContext());
        Calendar calendar = Calendar.getInstance();
        long timeStamp = calendar.getTimeInMillis();
        Call<List<ProgramItem>> callPrograms = HttpManager.getApiService().getProgramsList(timeStamp);
        callPrograms.enqueue(new Callback<List<ProgramItem>>() {
            @Override
            public void onResponse(Call<List<ProgramItem>> call, Response<List<ProgramItem>> response) {
                source.insertListPrograms(response.body());
                QueryPreferences.setCountLoadedDays(getApplicationContext(), 7);
                if (QueryPreferences.areCategoriesLoaded(getApplicationContext()) && QueryPreferences.areChannelsLoaded(getApplicationContext()))
                Log.i(TAG, "ProgramItemononHandleIntentResponse");
            }

            @Override
            public void onFailure(Call<List<ProgramItem>> call, Throwable t) {
                Log.i(TAG, "ProgramItemonFailure");
            }
        });
        NotificationAboutLoading.sendNotification(getApplicationContext(), getString(R.string.data_updated), 1);

    }
}




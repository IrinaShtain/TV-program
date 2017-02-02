package com.shtainyky.tvprogram.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.model.Program;
import com.shtainyky.tvprogram.parser.Parse;
import com.shtainyky.tvprogram.utils.Constants;
import com.shtainyky.tvprogram.httpconnection.HttpManager;
import com.shtainyky.tvprogram.utils.QueryPreferences;
import com.shtainyky.tvprogram.utils.CheckInternet;

import java.util.Calendar;
import java.util.List;


public class LoadingMonthProgramsIntentService extends IntentService {

    public LoadingMonthProgramsIntentService() {
        super("LoadingMonthProgramsIntentService");
    }


    public static Intent newIntent(Context context) {
        return new Intent(context, LoadingMonthProgramsIntentService.class);
    }

    public static void startService(Context context) {
        Intent intent = LoadingMonthProgramsIntentService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);
        Log.i("myLog", "startService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("myLog", "Received an intent: " + intent);
        if (QueryPreferences.getStoredFirstTimeService(getApplicationContext())) {
            Log.i("myLog", "Not startService");
            QueryPreferences.setStoredFirstTimeService(getApplicationContext(), false);
        } else {
            Log.i("myLog", "will startService");
            if (!CheckInternet.isOnline(getApplicationContext())) return;
            for (int i = 5; i < 31; i++) {
                Calendar calendar = Calendar.getInstance();
                DatabaseSource mSource = new DatabaseSource(getApplicationContext());
                calendar.add(Calendar.DAY_OF_YEAR, i);
                long timeStamp = calendar.getTimeInMillis();
                String response = HttpManager.getData(Constants.URI_PROGRAMS + timeStamp);
                List<Program> programs = Parse.parseJSONtoListPrograms(response);
                mSource.insertListPrograms(programs);
                Log.i("myLog", "HttpManager " + i);
            }
            QueryPreferences.setCountLoadedDays(getApplicationContext(), 30);
            NotificationAboutLoading.sendNotification(getApplicationContext(),
                    getResources().getString(R.string.data_are_loaded_for_period,
                            String.valueOf(30)),0);
        }
    }


}

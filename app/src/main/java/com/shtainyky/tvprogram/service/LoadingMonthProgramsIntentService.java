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
            for (int i = 1; i < 31; i++) {
                Calendar calendar = Calendar.getInstance();
                DatabaseSource mSource = new DatabaseSource(getApplicationContext());
                calendar.add(Calendar.DAY_OF_YEAR, i);
                long timeStamp = calendar.getTimeInMillis();
                String response = HttpManager.getData(Constants.URI_PROGRAMS + timeStamp);
                List<ProgramItem> programs = Parse.parseJSONtoListPrograms(response);
                mSource.insertListPrograms(programs);
                QueryPreferences.setCountLoadedDays(getApplicationContext(), i);
            }

            NotificationAboutLoading.sendNotification(getApplicationContext(),
                    getResources().getString(R.string.data_are_loaded_for_period,
                            String.valueOf(30)),0);
            Log.i("myLog", "HttpManager worked");
        }



}

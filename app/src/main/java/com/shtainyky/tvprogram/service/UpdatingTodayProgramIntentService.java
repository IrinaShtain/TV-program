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
import com.shtainyky.tvprogram.utils.CheckInternet;
import com.shtainyky.tvprogram.utils.QueryPreferences;

import java.util.Calendar;
import java.util.List;


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
        if (!CheckInternet.isOnline(getApplicationContext())) return;

        DatabaseSource mSource = new DatabaseSource(getApplicationContext());
        Calendar calendar = Calendar.getInstance();
        long timeStamp = calendar.getTimeInMillis();
        String response = HttpManager.getData(Constants.URI_PROGRAMS + timeStamp);
        List<ProgramItem> programs = Parse.parseJSONtoListPrograms(response);
        if (programs != null) {
            mSource.updateListPrograms(programs, programs.get(0).getDate());
            Log.i(TAG, " day +++++++++++++++++++" + programs.size());
        }
        NotificationAboutLoading.sendNotification(getApplicationContext(), getString(R.string.data_updated), 1);

    }
}




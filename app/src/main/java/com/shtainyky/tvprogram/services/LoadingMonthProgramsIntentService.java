package com.shtainyky.tvprogram.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.database.DatabaseStoreImp;
import com.shtainyky.tvprogram.models.retrofit.Program;
import com.shtainyky.tvprogram.services.httpconnection.HttpManager;
import com.shtainyky.tvprogram.utils.NotificationAboutLoading;
import com.shtainyky.tvprogram.utils.QueryPreferences;
import com.shtainyky.tvprogram.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
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
        if (!Utils.isOnline(getApplicationContext())) return;
        loadPrograms();
        NotificationAboutLoading.sendNotification(getApplicationContext(),
                getResources().getString(R.string.data_are_loaded_for_period,
                        String.valueOf(30)), 0);
        Log.i("myLog", "HttpManager worked");
    }

    private void loadPrograms() {
        Log.i("myLog", "loadPrograms begin");
        DatabaseStoreImp source = new DatabaseStoreImp(getApplicationContext());
        List<Program> programs = new ArrayList<>();
        for (int i = 1; i < 31; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, i);
            long timeStamp = calendar.getTimeInMillis();
            Call<List<Program>> callPrograms = HttpManager.getApiService().getProgramsList(timeStamp);

            try {
                Response<List<Program>> response = callPrograms.execute();
                programs.addAll(response.body());
                if (i % 10 == 0) {
                    Log.i("myLog", "ProgramItemonResponse Service %10" + i);
                    Log.i("myLog", "ProgramItemonResponse Serviceprograms.size() = " + programs.size());
                    source.insertListPrograms(programs);
                    QueryPreferences.setCountLoadedDays(getApplicationContext(), i);
                    programs.clear();
                    Log.i("myLog", "ProgramItemonResponse Service programs.size() = " + programs.size());
                }
                Log.i("myLog", "ProgramItemonResponse Service" + i);
            } catch (IOException e) {
                Log.i("myLog", "IOException Service" + i);
                e.printStackTrace();
            }
        }
    }


}

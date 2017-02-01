package com.shtainyky.tvprogram.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.model.Program;
import com.shtainyky.tvprogram.parser.Parse;
import com.shtainyky.tvprogram.utils.Constants;
import com.shtainyky.tvprogram.utils.QueryPreferences;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;


public class MyIntentService extends IntentService {
    public static final String TAG = "MyIntentService";

    public MyIntentService() {
        super(TAG);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);
//        DatabaseSource mSource = new DatabaseSource(getApplicationContext());
//        Calendar calendar = Calendar.getInstance();
//        for (int i = 1; i < 8; i++) {
//            calendar.add(Calendar.DAY_OF_YEAR, 1);
//            long timeStamp = calendar.getTimeInMillis();
//            String response = HttpManager.getData(Constants.URI_PROGRAMS + timeStamp);
//            List<Program> programs = Parse.parseJSONtoListPrograms(response);
//            mSource.insertListPrograms(programs);
//            Log.i(TAG," day ******************** = " + i);
//        }
        Calendar calendar = Calendar.getInstance();
        final DatabaseSource mSource = new DatabaseSource(getApplicationContext());
        for (int i = 1; i < 31; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            long timeStamp = calendar.getTimeInMillis();
            final int finalI = i;
            StringRequest request = new StringRequest(Constants.URI_PROGRAMS + timeStamp,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String utfString="";
                            try {
                                utfString = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                                List<Program> programs = Parse.parseJSONtoListPrograms(utfString);
                                if (programs != null){
                                    mSource.insertListPrograms(programs);
                                    Log.i(TAG, "UnsupportedEncodingException " + finalI);

                                }
                                else {
                                    Log.i(TAG, "NUULL " + utfString);
                                    Log.i(TAG, "NUULLresponse " + response);
                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                Log.i(TAG, "UnsupportedEncodingException " );
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(request);
            Toast.makeText(getApplicationContext(), R.string.data_are_loading_for_a_month, Toast.LENGTH_SHORT).show();
        }

        NotificationAboutLoading.sendNotification(getApplicationContext());
        QueryPreferences.setIsServiceWorking(getApplicationContext(), false);

    }
}




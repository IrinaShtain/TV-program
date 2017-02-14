package com.shtainyky.tvprogram.loading_data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.shtainyky.tvprogram.MainActivity;
import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.utils.QueryPreferences;
import com.shtainyky.tvprogram.utils.Utils;

public class LoadingDataActivity extends AppCompatActivity {
    private final static String TAG = "myLog";

    public final static int STATUS_FINISH = 200;
    public final static String BROADCAST_ACTION = "com.shtainyky.servicebackbroadcast";
    public final static String PARAM_STATUS = "status";
    BroadcastReceiver br;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadind_data);
        setupToolbarMenu();
        setupBroadcastReceiver();

        if (Utils.isOnline(this)) {
            Intent intents = new Intent(getApplicationContext(), LoadingDataIntentService.class);
            startService(intents);
        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.no_internet,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    private void setupBroadcastReceiver() {
        br = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                int status = intent.getIntExtra(PARAM_STATUS, 0);
                Log.i(TAG, "onReceive");
                if (status == STATUS_FINISH)
                    finishLoading();
            }
        };
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(br, intFilt);
    }

    private void setupToolbarMenu() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.loading_data);
    }

    private void finishLoading() {
        Toast.makeText(getApplicationContext(), R.string.data_are_loaded, Toast.LENGTH_SHORT).show();
        QueryPreferences.setStoredFirstInstallation(getApplicationContext(), false);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}

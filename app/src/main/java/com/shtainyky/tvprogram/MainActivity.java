package com.shtainyky.tvprogram;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.list_of_categories_displaying.ChannelListener;
import com.shtainyky.tvprogram.list_of_categories_displaying.ListOfCategoriesFragment;
import com.shtainyky.tvprogram.list_of_channels_displaying.ListOfChannelsFragment;
import com.shtainyky.tvprogram.list_of_channels_displaying.PreferredChannelListener;
import com.shtainyky.tvprogram.list_of_programs_displaying.TVProgramFragment;
import com.shtainyky.tvprogram.loading_data.LoadingDataActivity;
import com.shtainyky.tvprogram.services.UpdatingTodayProgramIntentService;
import com.shtainyky.tvprogram.utils.QueryPreferences;
import com.shtainyky.tvprogram.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ChannelListener, PreferredChannelListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigationView)
    NavigationView mNavigationView;

    DatabaseSource mSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mSource = new DatabaseSource(this);

        setupToolbarMenu();
        setupNavigationDrawerMenu();
        if (QueryPreferences.getStoredFirstInstallation(this)) {
            startLoadingActivity();
        } else {
            setFragment(new TVProgramFragment());
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_tv_program:
                closeDrawer();
                setFragment(TVProgramFragment.newInstance(false));
                break;
            case R.id.item_list_categories:
                closeDrawer();
                setFragment(ListOfCategoriesFragment.newInstance());
                break;
            case R.id.item_list_channels:
                closeDrawer();
                setFragment(ListOfChannelsFragment.newInstance(0, false));
                break;
            case R.id.item_list_preferred_channels:
                closeDrawer();
                setFragment(ListOfChannelsFragment.newInstance(0, true));
                break;
            case R.id.item_manual_sync:
                showDialogForManualSyns();
                break;
            case R.id.item_setup_update:
                showDialogTodaysUpdating();
                break;
        }
        return false;
    }

    private void showDialogForManualSyns() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tv_program_manual)
                .setMessage(getResources().getString(R.string.question_manual))
                .setCancelable(false)
                .setNegativeButton(getResources().getString(R.string.answer_no),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), R.string.cancel, Toast.LENGTH_SHORT).show();
                            }
                        })
                .setPositiveButton(getResources().getString(R.string.answer_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Utils.isOnline(getApplicationContext())) {
                                    startLoadingData();
                                } else {
                                    Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // TODO: 20.02.17 тіло методу не має перевищувати одного екрану, якщо більше, то можна розбити на менші методи
    private void showDialogTodaysUpdating() {
        String message;
        final boolean wasOn;
        // TODO: 20.02.17 check codestyle oracle{
        if (!QueryPreferences.getShouldUpdateDayProgram(this)) {
            message = getResources().getString(R.string.question_today_updating);
            wasOn = false;
        } else {
            message = getResources().getString(R.string.question_today_updating_is_on);
            wasOn = true;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tv_day_updating)
                .setMessage(message)
                .setCancelable(false)
                // TODO: 20.02.17 користувач і так знає що він натиснув, не треба тост
                .setNegativeButton(getResources().getString(R.string.answer_no),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),
                                        R.string.cancel,
                                        Toast.LENGTH_LONG).show();
                            }
                        })

                .setPositiveButton(getResources().getString(R.string.answer_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!wasOn) {
                                    QueryPreferences.setShouldUpdateDayProgram(getApplicationContext(), true);
                                    UpdatingTodayProgramIntentService.setServiceAlarm(getApplicationContext());
                                    Toast.makeText(getApplicationContext(),
                                            R.string.data_update_four_times,
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    QueryPreferences.setShouldUpdateDayProgram(getApplicationContext(), false);
                                    UpdatingTodayProgramIntentService.setServiceAlarm(getApplicationContext());
                                    Toast.makeText(getApplicationContext(),
                                            R.string.data_update_not_four_times,
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void startLoadingData() {
        mSource.deleteAllTables();
        Toast.makeText(this, R.string.data_syns, Toast.LENGTH_SHORT).show();
        QueryPreferences.setProgramLoaded(this, false);
        QueryPreferences.setChannelLoaded(this, false);
        QueryPreferences.setCategoryLoaded(this, false);
        startLoadingActivity();

    }

    private void startLoadingActivity() {
        Intent intent = new Intent(getApplicationContext(), LoadingDataActivity.class);
        startActivity(intent);
        finish();
    }

    private void setFragment(Fragment fragment) {
        // TODO: 20.02.17 m prefix mean member and can be used only for global variables, rename it
        FragmentManager mFragmentManager = getSupportFragmentManager();
        Fragment mFragment = mFragmentManager.findFragmentById(R.id.fragment_container);
        if (mFragment == null) {
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private void setupNavigationDrawerMenu() {
        mNavigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

    }

    private void setupToolbarMenu() {
        mToolbar.setTitle(R.string.app_name);
        if (Constants.Type.FREE == Constants.type) {
            // TODO: 20.02.17 don't need this, set the right string in build.gradle and get it from there
            if (Constants.Type.FREE == Constants.type) {
                mToolbar.setSubtitle("Free Version");
            } else {
                mToolbar.setSubtitle("Paid Version");
            }
        }
    }

        @Override
        // TODO: 20.02.17 you don't know this action name here, rename it
//    this action can be names as onCategoryClick or something like this
        public void setChannelsForCategoryId ( int categoryId, boolean isPreferred){
            ListOfChannelsFragment fragment = ListOfChannelsFragment.newInstance(categoryId, isPreferred);
            setFragment(fragment);
        }



        // TODO: 20.02.17 you don't know this action name here, rename it
//    this action can be names as onChannelClick or something like this
        @Override
        public void showPreferredChannels () {
            TVProgramFragment fragment = TVProgramFragment.newInstance(true);
            setFragment(fragment);
        }


}

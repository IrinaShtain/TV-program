package com.shtainyky.tvprogram.activities;

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

import com.shtainyky.tvprogram.BuildConfig;
import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.adapters.CategoriesRecyclerViewAdapter;
import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.fragments.ListOfCategoriesFragment;
import com.shtainyky.tvprogram.fragments.ListOfChannelsFragment;
import com.shtainyky.tvprogram.fragments.TVProgramFragment;
import com.shtainyky.tvprogram.services.UpdatingTodayProgramIntentService;
import com.shtainyky.tvprogram.utils.QueryPreferences;
import com.shtainyky.tvprogram.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ListOfChannelsFragment.OnPreferredChannelClickListener, CategoriesRecyclerViewAdapter.OnCategoryClickListener {

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
        if (QueryPreferences.isFirstInstallation(this)) {
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

    // 20.02.17 тіло методу не має перевищувати одного екрану, якщо більше, то можна розбити на менші методи
    private void showDialogTodaysUpdating() {
        String message;
        final boolean wasOn;
        // 20.02.17 check codestyle oracle{
        if (!QueryPreferences.isUpdatingDayProgramOn(this)) {
            message = getResources().getString(R.string.question_today_updating);
            wasOn = false;
        } else {
            message = getResources().getString(R.string.question_today_updating_is_on);
            wasOn = true;
        }
        showAlertDialog(message, wasOn);
    }

    private void showAlertDialog(String message, final boolean wasOnUpdating) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tv_day_updating)
                .setMessage(message)
                .setCancelable(false)
                // 20.02.17 користувач і так знає що він натиснув, не треба тост
                //Done
                .setNegativeButton(getResources().getString(R.string.answer_no),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })

                .setPositiveButton(getResources().getString(R.string.answer_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!wasOnUpdating) {
                                    startServiceAndShowToast(true, getString(R.string.data_update_four_times));
                                } else {
                                    startServiceAndShowToast(false, getString(R.string.data_dont_update_four_times));
                                }
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void startServiceAndShowToast(boolean updateDayProgram, String toastMessage) {
        QueryPreferences.setUpdatingDayProgram(getApplicationContext(), updateDayProgram);
        UpdatingTodayProgramIntentService.setServiceAlarm(getApplicationContext());
        Toast.makeText(getApplicationContext(),
                toastMessage,
                Toast.LENGTH_LONG).show();
    }

    private void startLoadingData() {
        mSource.deleteAllTables();
        Toast.makeText(this, R.string.data_syns, Toast.LENGTH_SHORT).show();
        QueryPreferences.setProgramsAreLoaded(this, false);
        QueryPreferences.setChannelsAreLoaded(this, false);
        QueryPreferences.setCategoriesAreLoaded(this, false);
        startLoadingActivity();

    }

    private void startLoadingActivity() {
        Intent intent = new Intent(getApplicationContext(), LoadingDataActivity.class);
        startActivity(intent);
        finish();
    }

    private void setFragment(Fragment fragment) {
        // 20.02.17 m prefix mean member and can be used only for global variables, rename it
        //Done
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragmentFromContainer = fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragmentFromContainer == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
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
    // 20.02.17 don't need this, set the right string in build.gradle and get it from there
    //Done
    private void setupToolbarMenu() {
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setSubtitle(BuildConfig.FLAVORS_VERSION);
    }


    //if categoryID equals zero all channels will be shown
    @Override
    public void onCategoryClick(int categoryID) {
        ListOfChannelsFragment fragment = ListOfChannelsFragment.newInstance(categoryID, false);
        setFragment(fragment);
    }

    // 20.02.17 you don't know this action name here, rename it
    // this action can be names as onChannelClick or something like this
    @Override
    public void onPreferredChannelClick() {
        TVProgramFragment fragment = TVProgramFragment.newInstance(true);
        setFragment(fragment);
    }
}

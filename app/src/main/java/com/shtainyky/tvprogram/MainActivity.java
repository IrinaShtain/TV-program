package com.shtainyky.tvprogram;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.shtainyky.tvprogram.database.DatabaseSource;
import com.shtainyky.tvprogram.fragments.ListOfCategoriesFragment;
import com.shtainyky.tvprogram.fragments.ListOfChannelsFragment;
import com.shtainyky.tvprogram.fragments.ListOfPreferredChannelsFragment;
import com.shtainyky.tvprogram.fragments.TVProgramFragment;
import com.shtainyky.tvprogram.utils.QueryPreferences;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private DatabaseSource mSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSource = new DatabaseSource(this);


        setupToolbarMenu();
        setupNavigationDrawerMenu();
        if (QueryPreferences.getStoredFirstInstallation(this))
        {
            startLoadingActivity();
        }
        else {
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
        closeDrawer();
        switch (item.getItemId()) {
            case R.id.item_tv_program:
                setFragment(new TVProgramFragment());
                break;
            case R.id.item_list_categories:
                setFragment(new ListOfCategoriesFragment());
                break;
            case R.id.item_list_channels:
                setFragment(new ListOfChannelsFragment());
                break;
            case R.id.item_list_preferred_channels:
                setFragment(new ListOfPreferredChannelsFragment());
                break;
            case R.id.item_manual_sync:
                if (QueryPreferences.getStoredIsServiceWorking(this))
                {
                    Toast.makeText(this, R.string.data_are_loading, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    startLoadingData();
                }
                break;
        }
        return false;
    }
    private void startLoadingData()
    {
        mSource.deleteAllTables();
        Toast.makeText(this, R.string.data_syns, Toast.LENGTH_SHORT).show();
        QueryPreferences.setProgramLoaded(this, false);
        QueryPreferences.setChannelLoaded(this, false);
        QueryPreferences.setCategoryLoaded(this, false);
        startLoadingActivity();

    }
    private void startLoadingActivity()
    {
        Intent intent = new Intent(getApplicationContext(), LoadingData.class);
        startActivity(intent);
        finish();
    }

    private void setFragment(Fragment fragment) {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        Fragment mFragment = mFragmentManager.findFragmentById(R.id.fragment_container);
        if (mFragment == null) {
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        } else {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    private void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private void setupNavigationDrawerMenu() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

    }

    private void setupToolbarMenu() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);
    }


}

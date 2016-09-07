package com.mcgowan.timetable.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mcgowan.timetable.android.sync.TimetableSyncAdapter;
import com.mcgowan.timetable.android.utility.Utility;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TIMETABLE_URL = "https://itsligo.ie/student-hub/my-timetable/";
    public static final String LABS_URL = "https://itsligo.ie/student-hub/computer-labs/";
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String SYNC_UPDATE = "SYNC_STATUS";

    private TabPagesAdapter mTabsPagesAdapter;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        switch (AppVersionCheck.checkAppStart(this)) {
//            case NORMAL:
//                // No need to do anything
//                break;
            case FIRST_TIME_VERSION:
                // TODO show what's new
                break;
            case FIRST_TIME:
                // TODO show a tutorial
                break;
            default:
                break;
        }
        TimetableSyncAdapter.initializeSyncAdapter(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SyncReceiver myReceiver = new SyncReceiver();
        IntentFilter intentFilter = new IntentFilter("com.mcgowan.timetable.android.syncComplete");
        registerReceiver(myReceiver, intentFilter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        String studentId = Utility.getStudentId(this);
        if (studentId.equals("")) {
            showNoStudentIdDialog();
        } else {
            mTabsPagesAdapter = new TabPagesAdapter(getSupportFragmentManager(), this);

            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mTabsPagesAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabbar);
            tabLayout.setupWithViewPager(mViewPager);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        getMenuInflater().inflate(R.menu.menu_timetablefragmemt, menu);
        //add fonts to all items
        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);
            Utility.applyFontToMenuItem(this, mi);
        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);
            Utility.applyFontToMenuItem(this, mi);
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return openSettingsDetail();
            case R.id.action_refresh:
                TimetableSyncAdapter.syncImmediately(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * launches settings as an intent
     *
     * @return
     */
    public boolean openSettingsDetail() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
        return true;
    }

    /**
     * No Student ID set dialog launcher
     */
    private void showNoStudentIdDialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_main, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).setTitle(getString(R.string.no_id_dialog_title));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openSettingsDetail();
            }
        });
        builder.create().show();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        int id = menuItem.getItemId();

        switch (id) {
            case R.id.nav_settings_general:
                drawer.closeDrawer(GravityCompat.START);
                launchSettingsActivity();
                break;

            case R.id.nav_settings_about:
                drawer.closeDrawer(GravityCompat.START);
                launchAboutActivity();
                break;

            case R.id.nav_settings_version:
                displayVersion();
                break;
            default:
                drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    private void launchSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void launchAboutActivity() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void displayVersion() {
        LayoutInflater inflater = LayoutInflater.from(this);

        View view = inflater.inflate(R.layout.dialog_main, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(view).setTitle(getString(R.string.app_version_title));

        TextView content = (TextView) view.findViewById(R.id.dialog_main_text_view);

        content.setText(BuildConfig.VERSION_NAME);

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    public class SyncReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            String status = extras.getString(SYNC_UPDATE);
            if(status != null) {
                Toast.makeText(context, status, Toast.LENGTH_LONG).show();
            }
        }
    }
}
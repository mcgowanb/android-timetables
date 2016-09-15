package com.mcgowan.timetable.android;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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

import com.mcgowan.timetable.android.sync.TimetableSyncAdapter;
import com.mcgowan.timetable.android.utility.Utility;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TIMETABLE_URL = "https://itsligo.ie/student-hub/my-timetable/";
    public static final String LABS_URL = "https://itsligo.ie/student-hub/computer-labs/";
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String SYNC_UPDATE = "SYNC_STATUS";
    public static final int NEW_INSTALL = 1;
    public static final int UPDATE_VERSION = 2;
    private SyncReceiver mSyncReciever;
    private ProgressDialog mProgress;
    private IntentFilter mSyncFilter;

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
                displayUpdateMessage(UPDATE_VERSION);
                break;
            case FIRST_TIME:
                displayUpdateMessage(NEW_INSTALL);
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

        mSyncReciever = new SyncReceiver();
        mSyncFilter = new IntentFilter(TimetableSyncAdapter.INTENT_SYNC_ACTION);
        registerReceiver(mSyncReciever, mSyncFilter);

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
        registerReceiver(mSyncReciever, mSyncFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mSyncReciever);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return true;
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

            case R.id.action_refresh:
                TimetableSyncAdapter.syncImmediately(this);
                drawer.closeDrawer(GravityCompat.START);
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

    private void displayLoading(String msg) {
        mProgress = ProgressDialog.show(this, "", msg, true);
    }

    private void dismissLoading(String status) {

        if (mProgress != null && mProgress.isShowing())
            mProgress.dismiss();
        Snackbar.make(findViewById(R.id.drawer_layout), status, Snackbar.LENGTH_LONG).show();

    }

    private void displayUpdateMessage(int state) {
        LayoutInflater inflater = LayoutInflater.from(this);

        View view = inflater.inflate(R.layout.dialog_main, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(view).setTitle(getString(R.string.welcome_title));

        TextView content = (TextView) view.findViewById(R.id.dialog_main_text_view);
        switch (state){
            case NEW_INSTALL:
                content.setText(R.string.welcome_message);
                break;
            case UPDATE_VERSION:
                content.setText(R.string.update_message);
                break;
            default:
                content.setText(R.string.welcome_message);
                break;
        }

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

            if (status.equals(TimetableSyncAdapter.LOADING_MESSAGE)) {
                displayLoading(status);
            } else {
                dismissLoading(status);
            }
        }
    }
}
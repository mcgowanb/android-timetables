package com.mcgowan.timetable.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mcgowan.timetable.android.sync.TimetableSyncAdapter;
import com.mcgowan.timetable.android.utility.Utility;


public class MainActivity extends AppCompatActivity {

    public static final String TIMETABLE_URL = "https://itsligo.ie/student-hub/my-timetable/";
    public static final String LABS_URL = "https://itsligo.ie/student-hub/computer-labs/";
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

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
//                Toast.makeText(this, "First time version", Toast.LENGTH_SHORT).show();
                // TODO show what's new
                break;
            case FIRST_TIME:
//                Toast.makeText(this, "First time", Toast.LENGTH_SHORT).show();
                // TODO show a tutorial
                break;
            default:
                break;
        }
        TimetableSyncAdapter.initializeSyncAdapter(this);
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
}
package com.mcgowan.timetable.itsligotimetables;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mcgowan.timetable.itsligotimetables.sync.TimetableSyncAdapter;


public class MainActivity extends AppCompatActivity {

    public static final String TIMETABLE_URL = "https://itsligo.ie/student-hub/my-timetable/";
    public static final String LABS_URL = "https://itsligo.ie/student-hub/computer-labs/";
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private final String TIMETABLEFRAGMENT_TAG = "TFTAG";
    private String mStudentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStudentId = Utility.getStudentId(this);
        setContentView(R.layout.activity_main);

        initMenuDetails();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new TimeTableFragment(), TIMETABLEFRAGMENT_TAG)
                    .commit();
        }

        TimetableSyncAdapter.initializeSyncAdapter(this);
    }

    private void initMenuDetails() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return openSettingsDetail();

            case R.id.action_map:
                return openMapDetails();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean openSettingsDetail() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
        return true;
    }

    public boolean openMapDetails() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        String studentId = Utility.getStudentId(this);

        if(studentId != null && !studentId.equals(mStudentId)){
            TimeTableFragment tf = (TimeTableFragment)getSupportFragmentManager()
                    .findFragmentByTag(TIMETABLEFRAGMENT_TAG);
            if(null != tf){
                tf.onStudentIdChanged();
            }
            mStudentId = studentId;
        }

    }



}

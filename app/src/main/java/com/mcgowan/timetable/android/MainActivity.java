package com.mcgowan.timetable.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mcgowan.timetable.android.sync.TimetableSyncAdapter;


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

        switch (AppVersionCheck.checkAppStart(this)) {
            case NORMAL:
                // No need to do anything
                break;
            case FIRST_TIME_VERSION:
                Toast.makeText(this,"First time version", Toast.LENGTH_SHORT).show();
                // TODO show what's new
                break;
            case FIRST_TIME:
                Toast.makeText(this,"First time", Toast.LENGTH_SHORT).show();
                // TODO show a tutorial
                break;
            default:
                break;
        }

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
//        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        for (int i=0;i<menu.size();i++) {
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean openSettingsDetail() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        String studentId = Utility.getStudentId(this);
        if(studentId.equals("")){
            showNoStudentIdDialog();
        }

        else if (studentId != null && !studentId.equals(mStudentId)){
            TimeTableFragment tf = (TimeTableFragment)getSupportFragmentManager()
                    .findFragmentByTag(TIMETABLEFRAGMENT_TAG);
            if(null != tf){
                tf.onStudentIdChanged();
            }
            mStudentId = studentId;
        }

    }

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

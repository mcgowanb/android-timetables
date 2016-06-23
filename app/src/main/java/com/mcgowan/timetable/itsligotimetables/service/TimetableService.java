package com.mcgowan.timetable.itsligotimetables.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.mcgowan.timetable.itsligotimetables.MainActivity;
import com.mcgowan.timetable.itsligotimetables.Utility;
import com.mcgowan.timetable.itsligotimetables.data.TimetableContract;
import com.mcgowan.timetable.scraper.Course;
import com.mcgowan.timetable.scraper.TimeTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class TimetableService extends IntentService {
    private ArrayAdapter<String> mTimetableAdapter;
    public static final String TIMETABLE_QUERY_EXTRA = "tqe";
    private String studentID;
    private final String LOG_TAG = TimetableService.class.getSimpleName();

    public TimetableService() {
        super("Timetable");
    }


    @Override
    protected void onHandleIntent(Intent intent) {


        String url = MainActivity.TIMETABLE_URL;
        studentID = intent.getStringExtra(TIMETABLE_QUERY_EXTRA);

        try {
            TimeTable t = new TimeTable(url, studentID);
            getClassesAsArray(t);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to ITS website", e);
        }
    }

    private void getClassesAsArray(TimeTable t) {
        List<String> classes = new ArrayList<String>();
        Map<String, List<Course>> days = t.getDays();

        Vector<ContentValues> cvVector = new Vector<>();

        for (Map.Entry<String, List<Course>> entry : days.entrySet()) {
            for (Course c : entry.getValue()) {
                ContentValues classValues = new ContentValues();
                classValues.put(TimetableContract.TimetableEntry.COLUMN_DAY, c.getDay());
                classValues.put(TimetableContract.TimetableEntry.COLUMN_LECTURER, c.getLecturer());
                classValues.put(TimetableContract.TimetableEntry.COLUMN_START_TIME, c.getStartTime());
                classValues.put(TimetableContract.TimetableEntry.COLUMN_TIME, c.getTime());
                classValues.put(TimetableContract.TimetableEntry.COLUMN_END_TIME, c.getEndTime());
                classValues.put(TimetableContract.TimetableEntry.COLUMN_STUDENT_ID, studentID);
                classValues.put(TimetableContract.TimetableEntry.COLUMN_SUBJECT, c.getSubject());
                classValues.put(TimetableContract.TimetableEntry.COLUMN_ROOM, c.getRoom());
                classValues.put(TimetableContract.TimetableEntry.COLUMN_DAY_ID, Utility.getDayNumberFromDay(c.getDay()));
                cvVector.add(classValues);

                String line = c.toString();
                classes.add(line);
            }
        }
        int inserted = 0;

        if (cvVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cvVector.size()];
            cvVector.toArray(cvArray);

            inserted = this.getContentResolver().bulkInsert(
                    TimetableContract.TimetableEntry.CONTENT_URI,
                    cvArray
            );

        }

        Uri timeTableUri = TimetableContract.TimetableEntry.buildTimetableWithStudentId(studentID);

        Cursor cursor = this.getContentResolver().query(timeTableUri, null, null, null, null);

        Log.d(LOG_TAG, "FetchTimetableService Complete. " + inserted + " records inserted");

        cvVector = new Vector<ContentValues>(cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                ContentValues cv = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, cv);
                cvVector.add(cv);
            } while (cursor.moveToNext());
        }
    }
}
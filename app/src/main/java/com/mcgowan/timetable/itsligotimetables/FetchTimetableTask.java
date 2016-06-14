package com.mcgowan.timetable.itsligotimetables;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.mcgowan.timetable.itsligotimetables.data.TimetableContract;
import com.mcgowan.timetable.itsligotimetables.data.TimetableContract.TimetableEntry;
import com.mcgowan.timetable.scraper.Course;
import com.mcgowan.timetable.scraper.TimeTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class FetchTimetableTask extends AsyncTask<String, Void, List<String>> {


    private final Context mContext;
    private ArrayAdapter mAdapter;

    public FetchTimetableTask(Context mContext, ArrayAdapter adapter) {

        this.mContext = mContext;
        this.mAdapter = adapter;
    }

    private boolean DEBUG = true;

    private final String LOG_TAG = FetchTimetableTask.class.getSimpleName();
    private String studentID;


    @Override
    protected List<String> doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        String url = params[0];
        studentID = params[1];

        try {
            TimeTable t = new TimeTable(url, studentID);
            List<String> classes = getClassesAsArray(t);

            return classes;

        } catch (IOException e) {
            Log.e(LOG_TAG, "Shit fell down");
            Log.e(LOG_TAG, "Error", e);
            return null;
        }
    }

    private List<String> getClassesAsArray(TimeTable t) {
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

            inserted = mContext.getContentResolver().bulkInsert(
                    TimetableEntry.CONTENT_URI,
                    cvArray
            );

        }

        Uri timeTableUri = TimetableEntry.buildTimetableWithStudentId(studentID);

//        Cursor cursor = mContext.getContentResolver().query(timeTableUri, null, null, null, null);

        Log.d(LOG_TAG, "FetchWeatherTask Complete. " + inserted + " records nserted");
//        cvVector = new Vector<ContentValues>(cursor.getCount());
//        if (cursor.moveToFirst()) {
//            do {
//                ContentValues cv = new ContentValues();
//                DatabaseUtils.cursorRowToContentValues(cursor, cv);
//                cvVector.add(cv);
//
//            } while (cursor.moveToNext());
//        }



        return classes;
    }

    @Override
    protected void onPostExecute(List<String> result) {
        if (result != null) {
            mAdapter.clear();
            mAdapter.addAll(result);
        }
    }
}

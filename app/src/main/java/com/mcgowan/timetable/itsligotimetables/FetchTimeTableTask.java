package com.mcgowan.timetable.itsligotimetables;

import android.os.AsyncTask;
import android.util.Log;

import com.mcgowan.timetable.scraper.TimeTable;

import java.io.IOException;

/**
 * Created by Brian on 02/06/2016.
 */
public class FetchTimeTableTask extends AsyncTask<Void, Void, Void> {
    private final String LOG_TAG = FetchTimeTableTask.class.getSimpleName();
    String STUDENT_URL = "https://itsligo.ie/student-hub/my-timetable/";
    String studentID = "S00165159";


    @Override
    protected Void doInBackground(Void... params) {
        String timetableData;
        try {
            TimeTable t = new TimeTable(STUDENT_URL, studentID);
            Log.e(LOG_TAG, t.toString());
            timetableData = t.toString();
            //return something

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error", e);
            return null;
        }
        return null;
    }
}


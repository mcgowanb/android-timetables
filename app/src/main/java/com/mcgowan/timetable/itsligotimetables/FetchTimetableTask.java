package com.mcgowan.timetable.itsligotimetables;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.mcgowan.timetable.scraper.Course;
import com.mcgowan.timetable.scraper.TimeTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FetchTimetableTask extends AsyncTask<String, Void, List<String>> {
    private final Context mContext;
    private ArrayAdapter adapter;

    public FetchTimetableTask(Context mContext, ArrayAdapter adapter){

        this.mContext = mContext;
        this.adapter = adapter;
    }

    private boolean DEBUG = true;

    private final String LOG_TAG = FetchTimetableTask.class.getSimpleName();


        @Override
        protected List<String> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String url = params[0];
            String studentID = params[1];

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

            for (Map.Entry<String, List<Course>> entry : days.entrySet()) {
                for (Course c : entry.getValue()) {
                    String line = c.toString();
                    classes.add(line);
                }
            }
            return classes;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            if (result != null) {
                adapter.clear();
                adapter.addAll(result);
            }
        }
//    }
}

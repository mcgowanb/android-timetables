package com.mcgowan.timetable.itsligotimetables;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mcgowan.timetable.scraper.*;

/**
 * A placeholder fragment containing a simple view.
 */
public class TimeTableFragment extends Fragment {

    ArrayAdapter<String> mTimetableAdapter;

    public TimeTableFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //enable fragment to handle menu events
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_timetablefragmemt, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String studentID = prefs.getString(getString(R.string.student_id_key),getString(R.string.student_id_default));

            FetchTimeTableTask task = new FetchTimeTableTask();
            task.execute(MainActivity.TIMETABLE_URL, studentID);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        List<String> sampleTimetable = new ArrayList<>();
        sampleTimetable.add("Class One");
        sampleTimetable.add("Class Two");
        sampleTimetable.add("Class Three");
        sampleTimetable.add("Class Four");
        sampleTimetable.add("Class Five");

        mTimetableAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.list_item_class,
                R.id.list_item_class_textview,
                sampleTimetable);

        ListView lv = (ListView) rootView.findViewById(R.id.listview_timetable);
        lv.setAdapter(mTimetableAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String lecture = mTimetableAdapter.getItem(position);
                Toast.makeText(getActivity(), lecture, Toast.LENGTH_SHORT).show();

                Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                detailIntent.putExtra("details", lecture);

               startActivity(detailIntent);
            }
        });

        return rootView;
    }


    class FetchTimeTableTask extends AsyncTask<String, Void, List<String>> {
        private final String LOG_TAG = FetchTimeTableTask.class.getSimpleName();


        @Override
        protected List<String> doInBackground(String... params) {
            String timetableData;

            if (params.length == 0) {
                return null;
            }

            String url = params[0];
            String studentID = params[1];

            try {
                TimeTable t = new TimeTable(url, studentID);
                timetableData = t.toString();
                List<String> classes = getClassesAsArray(t);

                return classes;

            } catch (IOException e) {
                Log.e(LOG_TAG, "Shit fell down");
                Log.e(LOG_TAG, "Error", e);
                return null;
            }
//        return null;
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
                mTimetableAdapter.clear();
                mTimetableAdapter.addAll(result);
            }
        }
    }
}


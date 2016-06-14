package com.mcgowan.timetable.itsligotimetables;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
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

import com.mcgowan.timetable.itsligotimetables.data.TimetableContract;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class TimeTableFragment extends Fragment {

    TimetableAdapter mTimetableAdapter;
    private static final String LOG_TAG = TimeTableFragment.class.getSimpleName();


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
            updateTimetable();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateTimetable();
    }

    private void updateTimetable() {
//        FetchTimetableTask task = new FetchTimetableTask(getActivity(), mTimetableAdapter);
//        task.execute(MainActivity.TIMETABLE_URL, studentID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String studentID = prefs.getString(getString(R.string.student_id_key),getString(R.string.student_id_default));


        Uri uri = TimetableContract.TimetableEntry.buildTimetableWithStudentId(studentID);
        Log.d(LOG_TAG, uri.toString());
        Cursor cur = getActivity().getContentResolver().query(uri, null, null, null, null);

        mTimetableAdapter = new TimetableAdapter(getActivity(), cur, 0);


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_timetable);
        listView.setAdapter(mTimetableAdapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String lecture = mTimetableAdapter.getItem(position);
//                Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
//                detailIntent.putExtra("details", lecture);
//
//               startActivity(detailIntent);
//            }
//        });

        return rootView;
    }
}


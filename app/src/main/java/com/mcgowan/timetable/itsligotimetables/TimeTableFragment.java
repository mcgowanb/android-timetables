package com.mcgowan.timetable.itsligotimetables;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mcgowan.timetable.itsligotimetables.data.TimetableContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class TimeTableFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    TimetableAdapter mTimetableAdapter;

    private static final String LOG_TAG = TimeTableFragment.class.getSimpleName();
    private static final int TIMETABLE_LOADER = 1;

    private static final String[] TIMETABLE_COLUMNS = {
            TimetableContract.TimetableEntry._ID,
            TimetableContract.TimetableEntry.COLUMN_DAY,
            TimetableContract.TimetableEntry.COLUMN_TIME,
            TimetableContract.TimetableEntry.COLUMN_START_TIME,
            TimetableContract.TimetableEntry.COLUMN_END_TIME,
            TimetableContract.TimetableEntry.COLUMN_LECTURER,
            TimetableContract.TimetableEntry.COLUMN_SUBJECT,
            TimetableContract.TimetableEntry.COLUMN_DAY_ID,
            TimetableContract.TimetableEntry.COLUMN_ROOM,
    };

    static final int COL_TIMETABLE_ID = 0;
    static final int COL_TIMETABLE_DAY = 1;
    static final int COL_TIMETABLE_TIME = 2;
    static final int COL_TIMETABLE_START_TIME = 3;
    static final int COL_TIMETABLE_END_TIME = 4;
    static final int COL_TIMETABLE_LECTURER = 5;
    static final int COL_TIMETABLE_SUBJECT = 6;
    static final int COL_TIMETABLE_DAY_ID = 7;
    static final int COL_TIMETABLE_ROOM = 8;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mTimetableAdapter = new TimetableAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView lv = (ListView) rootView.findViewById(R.id.listview_timetable);
        lv.setAdapter(mTimetableAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if(cursor != null){
                    Intent intent = new Intent(getActivity(), DetailActivity.class).
                            setData(TimetableContract.TimetableEntry
                                    .buildTimetableUri(cursor.getInt(COL_TIMETABLE_ID)));
                    startActivity(intent);
                }
            }
        });
        return rootView;
    }


    void onStudentIdChanged(){
        updateTimetable();
        getLoaderManager().restartLoader(TIMETABLE_LOADER, null, this);
    }


    public void updateTimetable() {
        String studentID = Utility.getStudentId(getActivity());
        FetchTimetableTask timetableTask = new FetchTimetableTask(getActivity());
        timetableTask.execute(studentID);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(TIMETABLE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String studentID = prefs.getString(getString(R.string.student_id_key), getString(R.string.student_id_default));
        Uri uri = TimetableContract.TimetableEntry.buildTimetableWithStudentId(studentID);

        Loader<Cursor> cx = new CursorLoader(getActivity(),
                uri,
                TIMETABLE_COLUMNS,
                null,
                null,
                null);
        return cx;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mTimetableAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTimetableAdapter.swapCursor(null);
    }
}


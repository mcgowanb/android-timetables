package com.mcgowan.timetable.android;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mcgowan.timetable.android.data.TimetableContract;
import com.mcgowan.timetable.android.sync.TimetableSyncAdapter;
import com.mcgowan.timetable.android.utility.Utility;

/**
 * A placeholder fragment containing a simple view.
 */
public class TimeTableTodayFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    TimetableAdapter mTimetableAdapter;

    private static final String LOG_TAG = TimeTableTodayFragment.class.getSimpleName();
    private static final int TIMETABLE_LOADER = 1;
    private int mPosition = ListView.INVALID_POSITION;
    private ListView mListView;
    private SharedPreferences.OnSharedPreferenceChangeListener mPrefsListener;
    private SharedPreferences mSharedPrefs;

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


    public TimeTableTodayFragment() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //enable fragment to handle menu events
        setHasOptionsMenu(true);
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
//        addPreferenceChangeListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mTimetableAdapter = new TimetableAdapter(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mListView = (ListView) rootView.findViewById(R.id.listview_timetable);
        View emptyView = rootView.findViewById(R.id.listview_empty);

        mListView.setEmptyView(emptyView);
        mListView.setAdapter(mTimetableAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    Intent intent = new Intent(getActivity(), LectureDetailsActivity.class).
                            setData(TimetableContract.TimetableEntry
                                    .buildTimetableUri(cursor.getInt(COL_TIMETABLE_ID)));
                    startActivity(intent);
                }

            }
        });
        return rootView;
    }


//    void onStudentIdChanged() {
//        getLoaderManager().restartLoader(TIMETABLE_LOADER, null, this);
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(TIMETABLE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String studentID = Utility.getStudentId(getContext());
        String dayId = Utility.getDayNumberAsString();

        Uri uri = TimetableContract.TimetableEntry.buildTimetableWithStudentIDAndDayID(studentID, dayId);

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
        mPosition = Utility.checkCursorForToday(cursor);

        mTimetableAdapter.swapCursor(cursor);
        updateEmptyView();
    }

    /**
     * Updates the view with empty message, contextually relevant to the network connection
     */
    public void updateEmptyView() {
        if (mTimetableAdapter.getCount() == 0) {
            TextView tv = (TextView) getView().findViewById(R.id.listview_empty);
            if (null != tv) {
                int message = R.string.no_info_available;
                @TimetableSyncAdapter.ServerStatus int serverStatus = Utility.getServerStatus(getContext());

                switch (serverStatus) {
                    case TimetableSyncAdapter.SERVER_STATUS_SERVER_DOWN:
                        message = R.string.server_down;
                        break;
                    case TimetableSyncAdapter.SERVER_STATUS_ID_INVALID:
                        message = R.string.server_invalid;
                        break;
                    case TimetableSyncAdapter.SERVER_STATUS_UNKNOWN:
                        message = R.string.server_unknown;
                    default:
                        if (!Utility.hasNetworkConnectivity(getActivity())) {
                            message = R.string.no_network;
                        }
                }
                tv.setText(message);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTimetableAdapter.swapCursor(null);
    }

    @Override
    public void onResume() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        sp.registerOnSharedPreferenceChangeListener(this);
        getLoaderManager().restartLoader(TIMETABLE_LOADER, null, this);
        super.onResume();
    }

    @Override
    public void onPause() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        sp.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.server_status_key))) {
            updateEmptyView();
        }
    }

    /**
     * adds listener for on change of preference settings
     */
//    private void addPreferenceChangeListener() {
//        mPrefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
//            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
//                if (key.equals(getResources().getString(R.string.student_id_key))) {
//                    onStudentIdChanged();
//                }
//            }
//        };
//        mSharedPrefs.registerOnSharedPreferenceChangeListener(mPrefsListener);
//    }
}


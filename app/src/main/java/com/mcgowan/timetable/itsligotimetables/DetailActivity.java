package com.mcgowan.timetable.itsligotimetables;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mcgowan.timetable.itsligotimetables.data.TimetableContract.*;

import static android.database.DatabaseUtils.dumpCursorToString;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_detail, new LectureDetailsFragment())
                    .commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static class LectureDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

        private static final String SHARE_CLASS_INFO = "#gettingMySmartOn";
        String classInformation;
        ShareActionProvider mShareActionProvider;
        private static final int DETAIL_LOADER = 2;
        private static final String LOG_TAG = LectureDetailsFragment.class.getSimpleName();

        private static final String[] TIMETABLE_COLUMNS = {
               TimetableEntry._ID,
               TimetableEntry.COLUMN_DAY,
               TimetableEntry.COLUMN_TIME,
               TimetableEntry.COLUMN_START_TIME,
               TimetableEntry.COLUMN_END_TIME,
               TimetableEntry.COLUMN_LECTURER,
               TimetableEntry.COLUMN_SUBJECT,
               TimetableEntry.COLUMN_DAY_ID,
               TimetableEntry.COLUMN_ROOM,
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

        public LectureDetailsFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

//            Intent intent = getActivity().getIntent();
//
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
//            if (intent != null) {
//                classInformation = intent.getDataString();
//                ((TextView) rootView.findViewById(R.id.detail_text)).setText(classInformation);
//            }

            return rootView;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {



            MenuItem menuItem = menu.findItem(R.id.action_share);

            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareTimetableIntent());
            } else {
                Log.d("ERRORORORORO", "Share Action Provider is null?");
            }

        }

        private Intent createShareTimetableIntent() {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    classInformation + SHARE_CLASS_INFO);
            return shareIntent;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            Log.d(LOG_TAG, "In onActivityCreated");
            getLoaderManager().initLoader(DETAIL_LOADER, null, this);
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Log.d(LOG_TAG, "In OnCreateLoader");
            Intent intent = getActivity().getIntent();
            if(intent == null){
                return null;
            }

            Uri x = intent.getData();

            CursorLoader cursorLoader = new CursorLoader(getActivity(),
                    intent.getData(),
                    TIMETABLE_COLUMNS,
                    null,
                    null,
                    null
            );
            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            Log.d(LOG_TAG, "In onLoadFinished");
            Log.d(LOG_TAG, dumpCursorToString(cursor));
            String result = String.format("%s : %s : %s : %s : %s : %s",
                    cursor.getString(LectureDetailsFragment.COL_TIMETABLE_ID),
                    cursor.getString(LectureDetailsFragment.COL_TIMETABLE_DAY_ID),
                    cursor.getString(LectureDetailsFragment.COL_TIMETABLE_DAY),
                    cursor.getString(LectureDetailsFragment.COL_TIMETABLE_TIME),
                    cursor.getString(LectureDetailsFragment.COL_TIMETABLE_SUBJECT),
                    cursor.getString(LectureDetailsFragment.COL_TIMETABLE_LECTURER)
            );

            TextView detailsView = (TextView)getView().findViewById(R.id.list_item_class_textview);
            detailsView.setText(result);

            if(mShareActionProvider != null){
                mShareActionProvider.setShareIntent(createShareTimetableIntent());
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }


}

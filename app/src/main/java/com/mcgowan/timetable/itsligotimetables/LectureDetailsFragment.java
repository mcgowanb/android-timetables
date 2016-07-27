package com.mcgowan.timetable.itsligotimetables;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcgowan.timetable.itsligotimetables.data.TimetableContract;

public class LectureDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String SHARE_CLASS_INFO = " #itsligo";
    private String mClassInformation;
    private static final int DETAIL_LOADER = 2;
    private static final String LOG_TAG = LectureDetailsFragment.class.getSimpleName();

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


    public LectureDetailsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void addFloatingActionBar() {
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = createShareTimetableIntent();
                startActivity(intent);
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        return rootView;
    }


    private Intent createShareTimetableIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                mClassInformation + SHARE_CLASS_INFO);
        return shareIntent;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
        addFloatingActionBar();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();
        if (intent == null || intent.getData() == null) {
            return null;
        }

        CursorLoader cursorLoader = new CursorLoader(
                getActivity(),
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
        if (!cursor.moveToFirst()) {
            return;
        }

        String startTime = cursor.getString(LectureDetailsFragment.COL_TIMETABLE_START_TIME);
        String endTime = cursor.getString(LectureDetailsFragment.COL_TIMETABLE_END_TIME);

        mClassInformation = String.format("%s, %s, %s.\nIn %s\nWith %s",
                cursor.getString(LectureDetailsFragment.COL_TIMETABLE_DAY),
                cursor.getString(LectureDetailsFragment.COL_TIMETABLE_TIME),
                cursor.getString(LectureDetailsFragment.COL_TIMETABLE_SUBJECT),
                cursor.getString(LectureDetailsFragment.COL_TIMETABLE_ROOM),
                cursor.getString(LectureDetailsFragment.COL_TIMETABLE_LECTURER)
        );

        setClockTime(startTime, endTime);

        TextView classNameView = (TextView) getView().findViewById(R.id.detail_class_name);
        Typeface face=Typeface.createFromAsset(getActivity().getAssets(),"fonts/RockSalt.ttf");
        classNameView.setTypeface(face);
        classNameView.setText(cursor.getString(LectureDetailsFragment.COL_TIMETABLE_SUBJECT));

        TextView lecturerNameView = (TextView) getView().findViewById(R.id.detail_lecturer_name);
        String lName = cursor.getString(LectureDetailsFragment.COL_TIMETABLE_LECTURER);
        lecturerNameView.setText(lName);

        TextView roomNameView = (TextView) getView().findViewById(R.id.detail_room_name);
        String rName = cursor.getString(LectureDetailsFragment.COL_TIMETABLE_ROOM);
        roomNameView.setText(rName);

        TextView startTimeView = (TextView) getView().findViewById(R.id.detail_time);
        String sTime = cursor.getString(LectureDetailsFragment.COL_TIMETABLE_START_TIME);
        String eTime = cursor.getString(LectureDetailsFragment.COL_TIMETABLE_END_TIME);
        startTimeView.setText(String.format("%s - %s", sTime, eTime));

        TextView endTimeView = (TextView) getView().findViewById(R.id.detail_day);
        String day = cursor.getString(LectureDetailsFragment.COL_TIMETABLE_DAY);
        endTimeView.setText(day);


    }

    private void setClockTime(String startTime, String endTime) {
        View currentView = getActivity().findViewById(R.id.fragment_cardView);
        FrameLayout layout = (FrameLayout) currentView.findViewById(R.id.clock_frame_layout);

        ImageView clockImage = new ImageView(getActivity());
        clockImage.setImageResource(R.drawable.time_blank);

        int startIdx = Integer.parseInt(startTime.substring(0, 2));
        int endIdx = Integer.parseInt(endTime.substring(0, 2));

        for (int i = startIdx; i < endIdx; i++) {
            ImageView timeImage = new ImageView(getActivity());
            timeImage.setImageResource(Utility.getImageForPeriod(getActivity(), i));
            layout.addView(timeImage);
        }

        layout.addView(clockImage);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
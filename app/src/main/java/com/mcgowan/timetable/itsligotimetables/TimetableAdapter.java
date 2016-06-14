package com.mcgowan.timetable.itsligotimetables;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.mcgowan.timetable.itsligotimetables.data.TimetableContract.TimetableEntry;

/**
 * Created by Brian on 12/06/2016.
 */
public class TimetableAdapter extends CursorAdapter {
    private static final String LOG_TAG = TimeTableFragment.class.getSimpleName();
    public TimetableAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    private String convertCursorRowToUXFormat(Cursor cursor) {
        // get row indices for our cursor
        String result = String.format("%s : %s : %s : %s : %s : %s",
                cursor.getString(TimeTableFragment.COL_TIMETABLE_ID),
                cursor.getString(TimeTableFragment.COL_TIMETABLE_DAY_ID),
                cursor.getString(TimeTableFragment.COL_TIMETABLE_DAY),
                cursor.getString(TimeTableFragment.COL_TIMETABLE_TIME),
                cursor.getString(TimeTableFragment.COL_TIMETABLE_SUBJECT),
                cursor.getString(TimeTableFragment.COL_TIMETABLE_LECTURER)
        );

        return result;
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_class, parent, false);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

        TextView tv = (TextView)view;
        tv.setText(convertCursorRowToUXFormat(cursor));
    }
}

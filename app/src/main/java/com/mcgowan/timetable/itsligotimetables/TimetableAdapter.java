package com.mcgowan.timetable.itsligotimetables;

import android.content.Context;
import android.database.Cursor;
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
    public TimetableAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /*
        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
        string.
     */
    private String convertCursorRowToUXFormat(Cursor cursor) {
        // get row indices for our cursor

        int idx_day = cursor.getColumnIndex(TimetableEntry.COLUMN_DAY);
        int idx_time = cursor.getColumnIndex(TimetableEntry.COLUMN_TIME);
        int idx_lecturer = cursor.getColumnIndex(TimetableEntry.COLUMN_LECTURER);
        int idx_subject = cursor.getColumnIndex(TimetableEntry.COLUMN_SUBJECT);


        String result = String.format("%s : %s : %s : %s", cursor.getString(idx_day),
                cursor.getString(idx_lecturer),
                cursor.getString(idx_subject),
                cursor.getString(idx_time));

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

package com.mcgowan.timetable.itsligotimetables.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mcgowan.timetable.itsligotimetables.data.TimetableContract.TimetableEntry;

import java.sql.Time;


public class TimetableDbHelper extends SQLiteOpenHelper {

    protected static final String DATABASE_NAME = "timetable.db";
    private static final int DATABASE_VERSION = 1;

    public TimetableDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_TIMETABLE_DATABASE = "CREATE TABLE " + TimetableContract.TimetableEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                TimetableEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TimetableEntry.COLUMN_DAY_ID + " INTEGER, " +
                TimetableEntry.COLUMN_STUDENT_ID + " TEXT NOT NULL, " +
                TimetableEntry.COLUMN_DAY + " TEXT NOT NULL, " +
                TimetableEntry.COLUMN_START_TIME + " TEXT NOT NULL, " +
                TimetableEntry.COLUMN_END_TIME + " TEXT NOT NULL, " +
                TimetableEntry.COLUMN_SUBJECT + " TEXT NOT NULL, " +
                TimetableEntry.COLUMN_LECTURER + " TEXT NOT NULL, " +
                TimetableEntry.COLUMN_ROOM + " TEXT, " +
                TimetableEntry.COLUMN_TIME + " TEXT " +
                ");";

        db.execSQL(SQL_CREATE_TIMETABLE_DATABASE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        db.execSQL("DROP TABLE IF EXISTS " + TimetableEntry.TABLE_NAME);
        onCreate(db);
    }
}

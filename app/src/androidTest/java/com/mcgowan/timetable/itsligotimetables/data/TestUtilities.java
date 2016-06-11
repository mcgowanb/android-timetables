package com.mcgowan.timetable.itsligotimetables.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.mcgowan.timetable.itsligotimetables.utils.PollingCheck;

import java.util.Map;
import java.util.Set;


public class TestUtilities extends AndroidTestCase {

    public static final String STUDENT_ID = "S00165159";
    static ContentValues createSingleClassTimetableValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(TimetableContract.TimetableEntry.COLUMN_DAY_ID, 3);
        testValues.put(TimetableContract.TimetableEntry.COLUMN_STUDENT_ID, STUDENT_ID);
        testValues.put(TimetableContract.TimetableEntry.COLUMN_DAY, "Wednesday");
        testValues.put(TimetableContract.TimetableEntry.COLUMN_START_TIME, "09:00");
        testValues.put(TimetableContract.TimetableEntry.COLUMN_END_TIME, "10:11");
        testValues.put(TimetableContract.TimetableEntry.COLUMN_LECTURER, "Person Name");
        testValues.put(TimetableContract.TimetableEntry.COLUMN_SUBJECT, "Mathematics");
        testValues.put(TimetableContract.TimetableEntry.COLUMN_TIME, "09:00 - 17:00");

        return testValues;
    }

    static long insertClassTimetableValues(SQLiteDatabase db, ContentValues testValues) {
        // insert our test records into the database
        long classRowId = db.insert(TimetableContract.TimetableEntry.TABLE_NAME, null, testValues);

        return classRowId;
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }


}



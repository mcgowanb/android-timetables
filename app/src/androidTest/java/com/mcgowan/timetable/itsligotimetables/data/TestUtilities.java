package com.mcgowan.timetable.itsligotimetables.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.mcgowan.timetable.itsligotimetables.data.TimetableContract;

import java.util.Map;
import java.util.Set;


public class TestUtilities extends AndroidTestCase {
    /*
        Students: You can uncomment this helper function once you have finished creating the
        LocationEntry part of the WeatherContract.
     */
    static ContentValues createSingleClassTimetableValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(TimetableContract.TimetableEntry.COLUMN_DAY_ID, 3);
        testValues.put(TimetableContract.TimetableEntry.COLUMN_STUDENT_ID, "S00165159");
        testValues.put(TimetableContract.TimetableEntry.COLUMN_DAY, "Wednesday");
        testValues.put(TimetableContract.TimetableEntry.COLUMN_START_TIME, "09:00");
        testValues.put(TimetableContract.TimetableEntry.COLUMN_END_TIME, "10:11");
        testValues.put(TimetableContract.TimetableEntry.COLUMN_LECTURER, "Person Name");
        testValues.put(TimetableContract.TimetableEntry.COLUMN_SUBJECT, "Mathematics");
        testValues.put(TimetableContract.TimetableEntry.COLUMN_ROOM, "Room");
        testValues.put(TimetableContract.TimetableEntry.COLUMN_TIME, "09:00 - 17:00");

        return testValues;
    }

    static long insertClassTimetableValues(SQLiteDatabase db, ContentValues testValues) {
        // insert our test records into the database
        long classRowId;
        classRowId = db.insert(TimetableContract.TimetableEntry.TABLE_NAME, null, testValues);

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

}

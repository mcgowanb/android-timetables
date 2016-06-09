package com.mcgowan.timetable.itsligotimetables.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.mcgowan.timetable.itsligotimetables.data.TimetableContract.TimetableEntry;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    public void setUp() {
        deleteTheDatabase();
    }


    void deleteTheDatabase() {
        mContext.deleteDatabase(TimetableDbHelper.DATABASE_NAME);
    }

    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(TimetableEntry.TABLE_NAME);

        mContext.deleteDatabase(TimetableDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new TimetableDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + TimetableContract.TimetableEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> scheduleColumnHashSet = new HashSet<String>();
        scheduleColumnHashSet.add(TimetableEntry._ID);
        scheduleColumnHashSet.add(TimetableEntry.COLUMN_DAY_ID);
        scheduleColumnHashSet.add(TimetableEntry.COLUMN_DAY);
        scheduleColumnHashSet.add(TimetableEntry.COLUMN_START_TIME);
        scheduleColumnHashSet.add(TimetableEntry.COLUMN_END_TIME);
        scheduleColumnHashSet.add(TimetableEntry.COLUMN_SUBJECT);
        scheduleColumnHashSet.add(TimetableEntry.COLUMN_LECTURER);
        scheduleColumnHashSet.add(TimetableEntry.COLUMN_ROOM);
        scheduleColumnHashSet.add(TimetableEntry.COLUMN_TIME);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            scheduleColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                scheduleColumnHashSet.isEmpty());
        db.close();
    }

    public void testInsertIntoTimetable() {
        SQLiteDatabase db = new TimetableDbHelper(mContext).getWritableDatabase();
        assertTrue(db.isOpen());

        ContentValues values = TestUtilities.createSingleClassTimetableValues();

        long rowID = TestUtilities.insertClassTimetableValues(db, values);
        assertEquals("Row value is not 1", 1, rowID);
        assertTrue("Error: Failure to insert Single class values", rowID != -1);

        Cursor cursor = db.query(
                TimetableEntry.TABLE_NAME,
                null,   //all rows
                null,   //columns for where
                null,   //values for where
                null,   //columns for group by
                null,   //columns to filter by row groups
                null    //order
        );

        assertTrue("No records returned from database", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: Location Query Validation Failed", cursor, values);

        assertFalse("More than one record returned from the database", cursor.moveToNext());
        cursor.close();
        db.close();
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

}

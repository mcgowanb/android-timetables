package com.mcgowan.timetable.itsligotimetables.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.mcgowan.timetable.itsligotimetables.data.TimetableContract.TimetableEntry;

/**
 * Created by Brian on 09/06/2016.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    /*
    This helper function deletes all records from both database tables using the database
    functions only.  This is designed to be used to reset the state of the database until the
    delete functionality is available in the ContentProvider.
    */
    public void deleteAllRecordsFromDB() {
        TimetableDbHelper dbHelper = new TimetableDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(TimetableEntry.TABLE_NAME, null, null);
        db.close();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                TimetableProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: WeatherProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + TimetableContract.CONTENT_AUTHORITY,
                    providerInfo.authority, TimetableContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: WeatherProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    public void testGetType() {
        // content://com.example.android.sunshine.app/weather/
        String type = mContext.getContentResolver().getType(TimetableEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
        assertEquals("Error: the TimetableEntry CONTENT_URI should return TimetableEntry.CONTENT_TYPE",
                TimetableEntry.CONTENT_TYPE, type);

        String studentID = "S00165159";
        // content://com.example.android.sunshine.app/weather?studentId=S12345678
        type = mContext.getContentResolver().getType(
                TimetableEntry.buildTimetableWithStudentId(studentID));
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
        assertEquals("Error: the TimetableEntry CONTENT_URI with location should return TimetableEntry.CONTENT_TYPE",
                TimetableEntry.CONTENT_TYPE, type);

        String day = "Friday";
        // content://com.example.android.sunshine.app/weather?studentId=S00165159&day=Friday
        type = mContext.getContentResolver().getType(
                TimetableEntry.buildTimetableWithStudentIdAndDay(studentID, day));
        // vnd.android.cursor.item/com.example.android.sunshine.app/weather/1419120000
        assertEquals("Error: the TimetableEntry CONTENT_URI with student id and date should return TimetableEntry.CONTENT_ITEM_TYPE",
                TimetableEntry.CONTENT_TYPE, type);


    }

    public void testBasicTimetableQueryById() {
        // insert our test records into the database
        TimetableDbHelper dbHelper = new TimetableDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createSingleClassTimetableValues();
        long rowId = TestUtilities.insertClassTimetableValues(db, testValues);

        assertTrue("Unable to Insert Timetable data into the Database", rowId != -1);


        // Test the basic content provider query
        Cursor timetableCursor = mContext.getContentResolver().query(
                TimetableEntry.buildTimetableUri(rowId),
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("Basic Timetable query with student id ", timetableCursor, testValues);

        db.close();
    }

    public void testBasicTimetableQueryByStudentID() {
        // insert our test records into the database
        TimetableDbHelper dbHelper = new TimetableDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String studentId = "S00165159";

        ContentValues testValues = TestUtilities.createSingleClassTimetableValues();
        long rowId = TestUtilities.insertClassTimetableValues(db, testValues);

        assertTrue("Unable to Insert Timetable data into the Database", rowId != -1);


        // Test the basic content provider query
        Cursor timetableCursor = mContext.getContentResolver().query(
                TimetableEntry.buildTimetableWithStudentId(studentId),
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("basic timetable query with student id", timetableCursor, testValues);

        db.close();
    }

    public void testBasicTimetableQueryNoResults() {
        // insert our test records into the database
        TimetableDbHelper dbHelper = new TimetableDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String studentId = "S12345678";

        ContentValues testValues = TestUtilities.createSingleClassTimetableValues();
        long rowId = TestUtilities.insertClassTimetableValues(db, testValues);

        assertTrue("Unable to Insert Timetable data into the Database", rowId != -1);
        // Test the basic content provider query
        Cursor timetableCursor = mContext.getContentResolver().query(
                TimetableEntry.buildTimetableWithStudentId(studentId),
                null,
                null,
                null,
                null
        );

        // Make sure cursor is empty
        assertFalse("Cursor has returned records from the database, should be null", timetableCursor.moveToNext());
        db.close();
    }

    public void testBasicTimetableQueryWithStudentIdAndDay() {
        // insert our test records into the database
        TimetableDbHelper dbHelper = new TimetableDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String studentId = "S00165159";
        String day = "Wednesday";

        ContentValues testValues = TestUtilities.createSingleClassTimetableValues();
        long rowId = TestUtilities.insertClassTimetableValues(db, testValues);

        assertTrue("Unable to Insert Timetable data into the Database", rowId != -1);
        // Test the basic content provider query
        Cursor timetableCursor = mContext.getContentResolver().query(
                TimetableEntry.buildTimetableWithStudentIdAndDay(studentId, day),
                null,
                null,
                null,
                null
        );

        // Make sure cursor is empty
        TestUtilities.validateCursor("testing query with id and day", timetableCursor, testValues);
        db.close();
    }

    public void testTimetableQueryWithStudentIdAndDayNoResultsIncorrectDay() {
        // insert our test records into the database
        TimetableDbHelper dbHelper = new TimetableDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String studentId = "S00165159";
        String day = "Thursday";

        ContentValues testValues = TestUtilities.createSingleClassTimetableValues();
        long rowId = TestUtilities.insertClassTimetableValues(db, testValues);

        assertTrue("Unable to Insert Timetable data into the Database", rowId != -1);
        // Test the basic content provider query
        Cursor timetableCursor = mContext.getContentResolver().query(
                TimetableEntry.buildTimetableWithStudentIdAndDay(studentId, day),
                null,
                null,
                null,
                null
        );

        // Make sure cursor is empty
        assertFalse("Cursor has returned records from the database, should be null", timetableCursor.moveToNext());
        db.close();
    }

    public void testInsertReadProvider() {
        ContentValues testValues = TestUtilities.createSingleClassTimetableValues();

        //register a content observer
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(TimetableEntry.CONTENT_URI, true, tco);
        Uri locationUri = mContext.getContentResolver().insert(TimetableEntry.CONTENT_URI, testValues);

        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long locationRowId = ContentUris.parseId(locationUri);

        assertTrue(locationRowId != -1);

        //Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                TimetableEntry.buildTimetableWithStudentId(TestUtilities.STUDENT_ID),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating Timetable entry.",
                cursor, testValues);


        //check that we can get no or different results from the database

    }

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                TimetableEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                TimetableEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from schedules table during delete", 0, cursor.getCount());
        cursor.close();

    }

    public void testDeleteRecords() {
        testInsertReadProvider();

        // Register a content observer for our Timetable delete.
        TestUtilities.TestContentObserver timetableObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(TimetableEntry.CONTENT_URI, true, timetableObserver);

        // Register a content observer for our labs delete.
//        TestUtilities.TestContentObserver weatherObserver = TestUtilities.getTestContentObserver();
//        mContext.getContentResolver().registerContentObserver(AvailableLabEntry.CONTENT_URI, true, weatherObserver);

        deleteAllRecordsFromProvider();

        // Students: If either of these fail, you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
        // delete.  (only if the insertReadProvider is succeeding)
        timetableObserver.waitForNotificationOrFail();
//        weatherObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(timetableObserver);
//        mContext.getContentResolver().unregisterContentObserver(weatherObserver);
    }

    public void testUpdateLocation() {
        // Create a new map of values, where column names are the keys
        ContentValues values = TestUtilities.createSingleClassTimetableValues();

        Uri locationUri = mContext.getContentResolver().
                insert(TimetableEntry.CONTENT_URI, values);
        long locationRowId = ContentUris.parseId(locationUri);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + locationRowId);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(TimetableEntry._ID, locationRowId);
        updatedValues.put(TimetableEntry.COLUMN_LECTURER, "Scooby Doo");

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor timetableCursor = mContext.getContentResolver().query(TimetableEntry.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        timetableCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                TimetableEntry.CONTENT_URI, updatedValues, TimetableEntry._ID + "= ?",
                new String[]{Long.toString(locationRowId)});
        assertEquals(count, 1);

        // Test to make sure our observer is called.  If not, we throw an assertion.
        //
        // Students: If your code is failing here, it means that your content provider
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        timetableCursor.unregisterContentObserver(tco);
        timetableCursor.close();

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                TimetableEntry.CONTENT_URI,
                null,   // projection
                TimetableEntry._ID + " = " + locationRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateClass record.  Error validating class entry update.",
                cursor, updatedValues);

        cursor.close();

        deleteAllRecordsFromDB();

    }


    public void testBulkInsert() {

        ContentValues[] bulkInsertContentValues = createBulkInsertClassValues();

        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver observer = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(TimetableEntry.CONTENT_URI, true, observer);

        int insertCount = mContext.getContentResolver().bulkInsert(TimetableEntry.CONTENT_URI, bulkInsertContentValues);

        observer.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(observer);

        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                TimetableEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order == by DATE ASCENDING
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext()) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating WeatherEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }

    static private final int BULK_INSERT_RECORDS_TO_INSERT = 10;

    static ContentValues[] createBulkInsertClassValues() {
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++) {
            ContentValues classValues = new ContentValues();
            classValues.put(TimetableContract.TimetableEntry.COLUMN_DAY_ID, 3);
            classValues.put(TimetableContract.TimetableEntry.COLUMN_STUDENT_ID, TestUtilities.STUDENT_ID);
            classValues.put(TimetableContract.TimetableEntry.COLUMN_DAY, "Wednesday");
            classValues.put(TimetableContract.TimetableEntry.COLUMN_START_TIME, "09:00");
            classValues.put(TimetableContract.TimetableEntry.COLUMN_END_TIME, "10:11");
            classValues.put(TimetableContract.TimetableEntry.COLUMN_LECTURER, "Person Name");
            classValues.put(TimetableContract.TimetableEntry.COLUMN_SUBJECT, "Mathematics");
            classValues.put(TimetableContract.TimetableEntry.COLUMN_ROOM, "Room");
            classValues.put(TimetableContract.TimetableEntry.COLUMN_TIME, "09:00 - 17:00");
            returnContentValues[i] = classValues;
        }
        return returnContentValues;
    }


}

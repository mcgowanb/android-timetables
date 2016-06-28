package com.mcgowan.timetable.itsligotimetables.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.mcgowan.timetable.itsligotimetables.MainActivity;
import com.mcgowan.timetable.itsligotimetables.R;
import com.mcgowan.timetable.itsligotimetables.Utility;
import com.mcgowan.timetable.itsligotimetables.data.TimetableContract;
import com.mcgowan.timetable.scraper.Course;
import com.mcgowan.timetable.scraper.TimeTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Brian on 23/06/2016.
 */
public class TimetableSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = TimetableSyncAdapter.class.getSimpleName();

    public TimetableSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "onPerformSync Called.");


        String url = MainActivity.TIMETABLE_URL;
        String studentID = Utility.getStudentId(getContext());

        try {
            TimeTable t = new TimeTable(url, studentID);
            getClassesAsArray(t, studentID);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to ITS website", e);
        }

    }

    //look to refactor this into a utility method
    private void getClassesAsArray(TimeTable t, String studentID) {
        List<String> classes = new ArrayList<String>();
        Map<String, List<Course>> days = t.getDays();

        Vector<ContentValues> cvVector = new Vector<>();

        for (Map.Entry<String, List<Course>> entry : days.entrySet()) {
            for (Course c : entry.getValue()) {
                ContentValues classValues = new ContentValues();
                classValues.put(TimetableContract.TimetableEntry.COLUMN_DAY, c.getDay());
                classValues.put(TimetableContract.TimetableEntry.COLUMN_LECTURER, c.getLecturer());
                classValues.put(TimetableContract.TimetableEntry.COLUMN_START_TIME, c.getStartTime());
                classValues.put(TimetableContract.TimetableEntry.COLUMN_TIME, c.getTime());
                classValues.put(TimetableContract.TimetableEntry.COLUMN_END_TIME, c.getEndTime());
                classValues.put(TimetableContract.TimetableEntry.COLUMN_STUDENT_ID, studentID);
                classValues.put(TimetableContract.TimetableEntry.COLUMN_SUBJECT, c.getSubject());
                classValues.put(TimetableContract.TimetableEntry.COLUMN_ROOM, c.getRoom());
                classValues.put(TimetableContract.TimetableEntry.COLUMN_DAY_ID, Utility.getDayNumberFromDay(c.getDay()));
                cvVector.add(classValues);

                String line = c.toString();
                classes.add(line);
            }
        }
        int inserted = 0;

        if (cvVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cvVector.size()];
            cvVector.toArray(cvArray);

            inserted = getContext().getContentResolver().bulkInsert(
                    TimetableContract.TimetableEntry.CONTENT_URI,
                    cvArray
            );

        }

        Uri timeTableUri = TimetableContract.TimetableEntry.buildTimetableWithStudentId(studentID);

        Cursor cursor = getContext().getContentResolver().query(timeTableUri, null, null, null, null);

        Log.d(LOG_TAG, "FetchTimetableService Complete. " + inserted + " records inserted");

        cvVector = new Vector<ContentValues>(cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                ContentValues cv = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, cv);
                cvVector.add(cv);
            } while (cursor.moveToNext());
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

        }
        return newAccount;
    }
}

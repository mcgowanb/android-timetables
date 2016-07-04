package com.mcgowan.timetable.itsligotimetables;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mcgowan.timetable.itsligotimetables.data.TimetableContract;
import com.mcgowan.timetable.itsligotimetables.sync.TimetableSyncAdapter;
import com.mcgowan.timetable.scraper.Course;
import com.mcgowan.timetable.scraper.TimeTable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

public class Utility {
    private static final String LOG_TAG = Utility.class.getSimpleName();

    public static int getDayNumberFromDay(String day) {
        int retVal;
        switch (day) {
            case "Monday":
                retVal = 1;
                break;
            case "Tuesday":
                retVal = 2;
                break;
            case "Wednesday":
                retVal = 3;
                break;
            case "Thursday":
                retVal = 4;
                break;
            case "Friday":
                retVal = 5;
                break;
            case "Saturday":
                retVal = 6;
                break;
            case "Sunday":
                retVal = 7;
                break;
            default:
                retVal = 0;
                break;
        }
        return retVal;
    }

    public static String getStudentId(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(
                context.getString(R.string.student_id_key),
                context.getString(R.string.student_id_default));
    }

    public static int getDayIcon(Context context, String name) {
        String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date());
        String fileName = String.format("%s_130", name.toLowerCase());

        if (!today.equals(name)) {
            fileName = fileName.concat("_bw");
        }
        return context.getResources().getIdentifier(fileName,
                "drawable", context.getPackageName());
    }

    public static int checkCursorForToday(Cursor c) {
        String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date());
        int position = -1;
        while (c.moveToNext()) {
            String day = c.getString(TimeTableFragment.COL_TIMETABLE_DAY);
            if (today.toLowerCase().equals(day.toLowerCase())) {
                position = c.getPosition();
                break;
            }
        }
        return position;
    }

    public static Vector<ContentValues> convertTimeTableToVector(TimeTable t, String studentID) {
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
            }
        }
        return cvVector;
    }

    public static void deleteRecordsFromDatabase(Context context, String studentId) {
        context.getContentResolver().delete(
                TimetableContract.TimetableEntry.CONTENT_URI,
                TimetableContract.TimetableEntry.COLUMN_STUDENT_ID + " = ?",
                new String[]{studentId}
        );
        Log.d(LOG_TAG, "Records deleted before insertion");
    }

    public static void deleteAllRecordsFromDatabase(Context context) {
        context.getContentResolver().delete(
                TimetableContract.TimetableEntry.CONTENT_URI,
                null,
                null
        );
        Log.d(LOG_TAG, "All Records deleted");
    }

    public static void addRecordsToDatabase(Context context, Vector<ContentValues> cvVector) {
        ContentValues[] cvArray = new ContentValues[cvVector.size()];
        cvVector.toArray(cvArray);
        int inserted;

        inserted = context.getContentResolver().bulkInsert(
                TimetableContract.TimetableEntry.CONTENT_URI,
                cvArray
        );
        Log.d(LOG_TAG, "addRecordsToDatabase Complete. " + inserted + " records inserted");
    }

    public static boolean hasNetworkConnectivity(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @SuppressWarnings("ResourceType")
    public static @TimetableSyncAdapter.ServerStatus int getServerStatus(Context c){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getInt(c.getString(R.string.server_status_key),
                TimetableSyncAdapter.SERVER_STATUS_UNKNOWN);
    }

    public static void resetServerStatus(Context c){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor spe = prefs.edit();
        spe.putInt(c.getString(R.string.server_status_key), TimetableSyncAdapter.SERVER_STATUS_UNKNOWN);
        spe.apply();
    }

    public static int getImageForStartTime(Context context, String time){
        int ref = Integer.parseInt(time.substring(0,2));
        String fileName;

        switch (ref){
            case 9:
                fileName = "time_nine";
                break;
            case 10:
                fileName = "time_ten";
                break;
            case 11:
                fileName = "time_eleven";
                break;
            case 12:
                fileName = "time_twelve";
                break;
            case 13:
                fileName = "time_one";
                break;
            case 14:
                fileName = "time_two";
                break;
            case 15:
                fileName = "time_three";
                break;
            case 16:
                fileName = "time_four";
                break;
            case 17:
                fileName = "time_five";
                break;
            case 18:
                fileName = "time_six";
                break;
            default:
                fileName = "time_blank";
                break;
        }

        return context.getResources().getIdentifier("time_blank",
                "drawable", context.getPackageName());
    }
}

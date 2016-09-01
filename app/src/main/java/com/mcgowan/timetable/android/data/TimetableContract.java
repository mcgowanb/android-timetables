package com.mcgowan.timetable.android.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.mcgowan.timetable.android.utility.ClassTime;
import com.mcgowan.timetable.android.utility.DateUtility;

public class TimetableContract {

    public static final String CONTENT_AUTHORITY = "com.mcgowan.timetable.itsligotimetables.app";
    private static final String LOG_TAG = TimetableContract.class.getSimpleName();
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    //todo have another lecture_icon here for the available labs data to build with

    //paths to match table names
    public static final String PATH_TIMETABLES = "timetable";
    public static final String PATH_AVAILABLE_LABS = "labs";

    public static final class AvailableLabEntry implements BaseColumns {

        public static final String TABLE_NAME = "labs";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_AVAILABLE_LABS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_AVAILABLE_LABS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_AVAILABLE_LABS;
        //field names & URI's

        public static final Uri buildLabsWithDay(String day) {
            return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_DAY, day).build();
        }

        public static final String COLUMN_DAY = "day";

        public static Uri buildLabsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


    public static final class TimetableEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TIMETABLES).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_TIMETABLES;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_TIMETABLES;

        public static final String TABLE_NAME = "schedule";
        public static final String COLUMN_DAY_ID = "day_id";
        public static final String COLUMN_STUDENT_ID = "student_id";
        public static final String COLUMN_DAY = "day";
        public static final String COLUMN_START_TIME = "start_time";
        public static final String COLUMN_END_TIME = "end_time";
        public static final String COLUMN_SUBJECT = "subject";
        public static final String COLUMN_LECTURER = "lecturer";
        public static final String COLUMN_ROOM = "room";
        public static final String COLUMN_TIME = "time";


        public static Uri buildTimetableUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildTimetableWithStudentId(String studentID) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_STUDENT_ID, studentID).build();
        }

        public static Uri buildTimetableWithStudentIdAndDay(String studentId, String day) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_DAY, day)
                    .appendQueryParameter(COLUMN_STUDENT_ID, studentId)
                    .build();
        }

        public static String getStudentIdFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_STUDENT_ID);
        }

        public static Uri buildTimetableWithStudentIDAndDayID(String studentId, String dayID) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_STUDENT_ID, studentId)
                    .appendQueryParameter(COLUMN_DAY_ID, dayID)
                    .build();
        }

        public static Uri buildNextClassUri(String studentID){
            int dayID = DateUtility.getTodayId();
            String time = DateUtility.getTime();
            ClassTime classTime = new ClassTime(dayID, time);
            classTime.checkTimeAndDateForNextClass();

            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_DAY_ID, classTime.getDayIDAsString())
                    .appendQueryParameter(COLUMN_START_TIME, classTime.getCurrentTime())
                    .appendQueryParameter(COLUMN_STUDENT_ID, studentID)
                    .build();
        }

        public static String getDayFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_DAY);
        }

        public static String getDayIDFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_DAY_ID);
        }

        public static String getStartTimeFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_START_TIME);
        }
    }
}

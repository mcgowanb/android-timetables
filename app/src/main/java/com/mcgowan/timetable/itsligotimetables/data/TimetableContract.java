package com.mcgowan.timetable.itsligotimetables.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class TimetableContract {

    public static final String CONTENT_AUTHORITY = "com.mcgowan.timetable.itslitotimetables.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //paths to match table names
    public static final String PATH_TIMETABLES = "schedule";
    public static final String PATH_LABS = "labs";

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



        public static Uri buildTimetableWithStudentId(String studentID){
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_STUDENT_ID, studentID).build();
        }

        public static Uri buildTimeTableWithDayAndStudentId(String day, String studentId){
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_DAY, day)
                    .appendQueryParameter(COLUMN_STUDENT_ID, studentId)
                    .build();
        }

        public static String getStudentIdFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }

    }
}

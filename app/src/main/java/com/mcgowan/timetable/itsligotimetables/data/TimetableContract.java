package com.mcgowan.timetable.itsligotimetables.data;

import android.provider.BaseColumns;

public class TimetableContract {

    public static final class TimetableEntry implements BaseColumns{
        public static final String TABLE_NAME = "schedule";

        public static final String COLUMN_DAY_ID = "day_id";
        public static final String COLUMN_DAY = "day";
        public static final String COLUMN_START_TIME = "start_time";
        public static final String COLUMN_END_TIME = "end_time";
        public static final String COLUMN_SUBJECT = "subject";
        public static final String COLUMN_LECTURER = "lecturer";
        public static final String COLUMN_ROOM = "room";
        public static final String COLUMN_TIME = "time";

    }
}

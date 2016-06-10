package com.mcgowan.timetable.itsligotimetables.data;

import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;


public class TestTimetableContract extends AndroidTestCase {

    public static final String STUDENT_ID = "S00165159";
    public static final String DAY = "Monday";
    public static final String LOG_TAG = TestTimetableContract.class.getSimpleName();

    public void testBuildTimeTableStudentId() {
        Uri studentUri = TimetableContract.TimetableEntry.buildTimetableWithStudentId(STUDENT_ID);

        assertNotNull("Error: Null Uri returned.  You must fill-in buildTimeTableWithStudents in " +
                "TimetableContract.", studentUri);
        assertEquals("Error: Student ID not properly appended to the end of the Uri",
                STUDENT_ID, studentUri.getQueryParameters(TimetableContract.TimetableEntry.COLUMN_STUDENT_ID).);
//        assertEquals("Error: Weather location Uri doesn't match our expected result",
//                locationUri.toString(),
//                "content://com.example.android.sunshine.app/weather/%2FNorth%20Pole");
    }

}

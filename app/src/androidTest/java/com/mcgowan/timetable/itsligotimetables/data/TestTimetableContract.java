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
                STUDENT_ID, studentUri.getQueryParameter(TimetableContract.TimetableEntry.COLUMN_STUDENT_ID));



//        assertEquals("Error: Weather location Uri doesn't match our expected result",
//                locationUri.toString(),
//                "content://com.example.android.sunshine.app/weather/%2FNorth%20Pole");
    }

    public void testBuildTimetableWithStudentIdAndDay(){
        Uri studentUri = TimetableContract.TimetableEntry.buildTimeTableWithDayAndStudentId(STUDENT_ID, DAY);

        assertEquals("Error: Student ID not properly appended to the end of the Uri",
                STUDENT_ID, studentUri.getQueryParameter(TimetableContract.TimetableEntry.COLUMN_STUDENT_ID));

        assertEquals("Error: Day Field not properly appended to the end of the Uri",
                DAY, studentUri.getQueryParameter(TimetableContract.TimetableEntry.COLUMN_DAY));
    }

    public void testGetStudentIdFromUri(){
        Uri uri = TimetableContract.TimetableEntry.buildTimetableWithStudentId(STUDENT_ID);
        String res = TimetableContract.TimetableEntry.getStudentIdFromUri(uri);
        assertEquals(STUDENT_ID, res);
    }

    public void testGetDayIdFromUri(){
        Uri uri = TimetableContract.TimetableEntry.buildTimeTableWithDayAndStudentId(STUDENT_ID, DAY);
        String res = TimetableContract.TimetableEntry.getDayFromUri(uri);
        assertEquals(DAY, res);
    }

}

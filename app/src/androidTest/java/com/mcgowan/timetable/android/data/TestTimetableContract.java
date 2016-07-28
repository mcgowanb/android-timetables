package com.mcgowan.timetable.android.data;

import android.net.Uri;
import android.test.AndroidTestCase;


public class TestTimetableContract extends AndroidTestCase {

    public static final String STUDENT_ID = "S00165159";
    public static final String TIMETABLE_REF = "timetable";
    public static final String LABS_REF = "labs";
    public static final String DAY = "Monday";
    public static final String LOG_TAG = TestTimetableContract.class.getSimpleName();

    public void testBuildTimeTableStudentId() {
        Uri studentUri = TimetableContract.TimetableEntry.buildTimetableWithStudentId(STUDENT_ID);

        assertEquals("pointer for timetabls table is incorrect", TIMETABLE_REF, studentUri.getLastPathSegment());

        assertNotNull("Error: Null Uri returned.  You must fill-in buildTimeTableWithStudents in " +
                "TimetableContract.", studentUri);

        assertEquals("Error: Student ID not properly appended to the end of the Uri",
                STUDENT_ID, studentUri.getQueryParameter(TimetableContract.TimetableEntry.COLUMN_STUDENT_ID));

    }

    public void testBuildLabsUri() {
        Uri labsUri = TimetableContract.AvailableLabEntry.CONTENT_URI;
        assertEquals("pointer for labs table is incorrect", LABS_REF, labsUri.getLastPathSegment());
    }

    public void testBuildLabsUriWithDay() {
        Uri labsUri = TimetableContract.AvailableLabEntry.buildLabsWithDay(DAY);
        assertEquals("Day not correctly appended to the Uri",
                DAY, labsUri.getQueryParameter(TimetableContract.AvailableLabEntry.COLUMN_DAY));
    }

    public void testBuildTimetableWithStudentIdAndDay() {
        Uri studentUri = TimetableContract.TimetableEntry.buildTimetableWithStudentIdAndDay(STUDENT_ID, DAY);

        assertEquals("Error: Student ID not properly appended to the end of the Uri",
                STUDENT_ID, studentUri.getQueryParameter(TimetableContract.TimetableEntry.COLUMN_STUDENT_ID));

        assertEquals("Error: Day Field not properly appended to the end of the Uri",
                DAY, studentUri.getQueryParameter(TimetableContract.TimetableEntry.COLUMN_DAY));
    }

    public void testGetStudentIdFromUri() {
        Uri uri = TimetableContract.TimetableEntry.buildTimetableWithStudentId(STUDENT_ID);
        String res = TimetableContract.TimetableEntry.getStudentIdFromUri(uri);
        assertEquals(STUDENT_ID, res);
    }

    public void testGetDayIdFromUri() {
        Uri uri = TimetableContract.TimetableEntry.buildTimetableWithStudentIdAndDay(STUDENT_ID, DAY);
        String res = TimetableContract.TimetableEntry.getDayFromUri(uri);
        assertEquals(DAY, res);
    }

}

package com.mcgowan.timetable.android.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

public class TestUriMatcher extends AndroidTestCase {

    private static final Uri TEST_TIMETABLE_DIR = TimetableContract.TimetableEntry.CONTENT_URI;
    private static final Uri TEST_LABS_DIR = TimetableContract.AvailableLabEntry.CONTENT_URI;


    public void testUriMatcher() {
        UriMatcher testMatcher = TimetableProvider.buildUriMatcher();
        assertEquals("Error: The TIMETABLE URI was matched incorrectly.",
                testMatcher.match(TEST_TIMETABLE_DIR), TimetableProvider.TIMETABLE);

        assertEquals("Error: The Labs URI was matched incorrectly.",
                testMatcher.match(TEST_LABS_DIR),
                TimetableProvider.LABS);

    }

}

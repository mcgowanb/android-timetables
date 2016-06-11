package com.mcgowan.timetable.itsligotimetables.utils;

import android.test.AndroidTestCase;


public class TestUtility extends AndroidTestCase {

    private static String[] days = {"", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    public void testDayNumbers() {

        for(int i = 1; i < days.length; i++){
            assertEquals(i + " Failed to be returned from " + days[i], i, Utility.getDayNumberFromDay(days[i]));
        }

        assertEquals("Failed to return a default value of 0", 0, Utility.getDayNumberFromDay("SomethingELse"));
    }


}

package com.mcgowan.timetable.android.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DateUtility {

    public static int getTodayId() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        return calendar.get(Calendar.DAY_OF_WEEK) -1;
    }

    public static String getTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(c.getTime());
    }
}
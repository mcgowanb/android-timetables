package com.mcgowan.timetable.android.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class DateUtility {

    public static int getTodayId() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        return calendar.get(Calendar.DAY_OF_WEEK) -1;
    }

    public static String getTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.UK);
        return sdf.format(c.getTime());
    }
}
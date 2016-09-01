package com.mcgowan.timetable.android.utility;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ClassTime {

    private String currentTime;
    private int dayID;
    private static final String LOG_TAG = ClassTime.class.getSimpleName();
    private static final String TIME_THRESHOLD = "17:00";
    private static final String TIME_START = "09:00";

    public ClassTime(int dayID, String currentTime) {
        this.dayID = dayID;
        this.currentTime = currentTime;
    }

    public String getCurrentTime() {
        return currentTime;
    }


    public String getDayIDAsString() {
        return String.valueOf(dayID);
    }


    public void checkTimeAndDateForNextClass() {
        try {

            Date date1 = new SimpleDateFormat("HH:mm", Locale.UK).parse(currentTime);
            Calendar nowCalendar = Calendar.getInstance();
            nowCalendar.setTime(date1);

            Date date2 = new SimpleDateFormat("HH:mm", Locale.UK).parse(TIME_THRESHOLD);
            Calendar lastClassCalendar = Calendar.getInstance();
            lastClassCalendar.setTime(date2);

            if (nowCalendar.getTime().after(lastClassCalendar.getTime())) {
                dayID++;
                if(dayID > 6) dayID = 1;
                currentTime = TIME_START;
            }


        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error parsing times with : " + e.getMessage());
        }
    }
}

package com.mcgowan.timetable.itsligotimetables;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {
    private static final String LOG_TAG = Utility.class.getSimpleName();

    public static int getDayNumberFromDay(String day) {
        int retVal;
        switch (day) {
            case "Monday":
                retVal = 1;
                break;
            case "Tuesday":
                retVal = 2;
                break;
            case "Wednesday":
                retVal = 3;
                break;
            case "Thursday":
                retVal = 4;
                break;
            case "Friday":
                retVal = 5;
                break;
            case "Saturday":
                retVal = 6;
                break;
            case "Sunday":
                retVal = 7;
                break;
            default:
                retVal = 0;
                break;
        }
        return retVal;
    }

    public static String getStudentId(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String studentId = prefs.getString(
                context.getString(R.string.student_id_key),
                context.getString(R.string.student_id_default));

        return studentId;
    }


    public static int getDayImageFromDayString(String day){
        int imageId;
        String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date());
        switch (day){
            case "Monday":
                imageId = R.drawable.monday_48;
                break;
            case "Tuesday":
                imageId = R.drawable.tuesday_48;
                break;
            case "Wednesday":
                imageId = R.drawable.wednesday_48;
                break;
            case "Thursday":
                imageId = R.drawable.thursday_48;
                break;
            case "Friday":
                imageId = R.drawable.friday_48;
                break;
            case "Saturday":
                imageId = R.drawable.saturday_48;
                break;
            case "Sunday":
                imageId = R.drawable.sunday_48;
                break;
            default:
                imageId = R.mipmap.ic_launcher;
                break;
        }
        return imageId;
    }


}

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

    public static String getStudentId(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String studentId = prefs.getString(
                context.getString(R.string.student_id_key),
                context.getString(R.string.student_id_default));

        return studentId;
    }

    public static int getDayIcon(Context context, String name) {
        String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date());
        String fileName = String.format("%s_130", name.toLowerCase());

        if(!today.equals(name)){
            fileName = fileName.concat("_bw");
        }
        return context.getResources().getIdentifier(fileName,
                "drawable", context.getPackageName());
    }

}

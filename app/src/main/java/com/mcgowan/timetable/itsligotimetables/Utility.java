package com.mcgowan.timetable.itsligotimetables;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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


    public static String createTimeAndSubject(String time, String subject){
        return String.format("%s \t| %s", time, subject);
    }

    public static String createRoomAndLecturerString(String room, String lecturer){
        return String.format("Room %s \t| Lecturer %s", room, lecturer);
    }

    public static int getDayImageFromDayString(String day){
        int imageId;
        switch (day){
            case "Monday":
                imageId = R.drawable.monday_icon;
                break;
            case "Tuesday":
                imageId = R.drawable.tuesday_icon;
                break;
            case "Wednesday":
                imageId = R.drawable.wednesday_icon;
                break;
            case "Thursday":
                imageId = R.drawable.thursday_icon;
                break;
            case "Friday":
                imageId = R.drawable.friday_icon;
                break;
            default:
                imageId = R.mipmap.ic_launcher;
                break;
        }
        return imageId;
    }


}

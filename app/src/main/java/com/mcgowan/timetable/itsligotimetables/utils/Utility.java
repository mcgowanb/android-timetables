package com.mcgowan.timetable.itsligotimetables.utils;

/**
 * Created by Brian on 11/06/2016.
 */
public class Utility {

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
}

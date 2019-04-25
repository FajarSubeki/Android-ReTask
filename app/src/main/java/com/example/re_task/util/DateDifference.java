package com.example.re_task.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateDifference {

    static SimpleDateFormat sdFormat = new SimpleDateFormat("dd-MM-yyyy");
    public static long betweenDates(String firstDate, String secondDate){
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));

        long daysDiff=0;
        long timeDiff=0;
        try {
            Date startDateObj = sdFormat.parse(firstDate);
            Date endDateObj = sdFormat.parse(secondDate);
            timeDiff = endDateObj.getTime() - startDateObj.getTime();
            daysDiff = timeDiff/(1000*60*60*24);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return daysDiff;
    }

}

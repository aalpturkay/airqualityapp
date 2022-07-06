package com.alpturkay.airqualityapp.gen.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CustomDateUtil {
    public static Long convertDateToUnixTimeStamp(Date date){
        return date.getTime() / 1000;
    }

    public static String getLastWeek(SimpleDateFormat dateFormat){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        return dateFormat.format(calendar.getTime());
    }

    public static Date parseStringToDate(String date, SimpleDateFormat dateFormat){
        Date parsedDate;
        try {
            parsedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return parsedDate;
    }
}

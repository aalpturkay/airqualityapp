package com.alpturkay.airqualityapp.gen.utils;

import com.alpturkay.airqualityapp.gen.enums.GenErrorMessage;
import com.alpturkay.airqualityapp.gen.exceptions.BaseErrorMessage;
import com.alpturkay.airqualityapp.gen.exceptions.GenBusinessException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CustomDateUtil {

    public static final String MIN_QUERYABLE_DATE = "27-11-2020";

    public static Long convertDateToUnixTimeStamp(Date date){
        if (date == null)
            throw new GenBusinessException(GenErrorMessage.DATE_COULD_NOT_BE_CONVERTED);
        return date.getTime() / 1000;
    }

    public static String getLastWeek(SimpleDateFormat dateFormat){

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);

        return dateFormat.format(calendar.getTime());
    }

    public static boolean isBetweenMinDateAndToday(String date, String minDate, SimpleDateFormat dateFormat){
        Date min, queriedDate;

        min = parseStringToDate(minDate, dateFormat);
        queriedDate = parseStringToDate(date, dateFormat);

        Calendar todayCal = Calendar.getInstance();
        Calendar minCal = Calendar.getInstance();
        Calendar queriedCal = Calendar.getInstance();
        todayCal.setTime(new Date());
        minCal.setTime(min);
        queriedCal.setTime(queriedDate);

        return queriedCal.before(todayCal) && queriedCal.after(minCal);
    }

    public static Date parseStringToDate(String date, SimpleDateFormat dateFormat){

        Date parsedDate;

        try {
            parsedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            throw new GenBusinessException(GenErrorMessage.DATE_COULD_NOT_BE_PARSED);
        }

        return parsedDate;
    }

}

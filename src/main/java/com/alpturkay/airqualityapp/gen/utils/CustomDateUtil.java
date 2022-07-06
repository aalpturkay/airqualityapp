package com.alpturkay.airqualityapp.gen.utils;

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
}

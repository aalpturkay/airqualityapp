package com.alpturkay.airqualityapp.gen.utils;

import java.util.Date;

public class CustomDateUtil {
    public static Long convertDateToUnixTimeStamp(Date date){
        return date.getTime() / 1000;
    }
}

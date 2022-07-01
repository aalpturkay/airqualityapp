package com.alpturkay.airqualityapp.gen.utils;

import org.springframework.util.StringUtils;

public class CustomStringUtil {
    public static String lowerAndCapitalizeFirstLetter(String text){
        return StringUtils.capitalize(text.toLowerCase());
    }
}

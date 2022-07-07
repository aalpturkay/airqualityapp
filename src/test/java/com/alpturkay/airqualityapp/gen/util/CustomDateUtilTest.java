package com.alpturkay.airqualityapp.gen.util;

import com.alpturkay.airqualityapp.gen.enums.GenErrorMessage;
import com.alpturkay.airqualityapp.gen.exceptions.GenBusinessException;
import com.alpturkay.airqualityapp.gen.utils.CustomDateUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import static org.junit.jupiter.api.Assertions.*;

public class CustomDateUtilTest {

    private static Date date;
    private static SimpleDateFormat dateFormat;
    private static String dateStr;
    @BeforeAll
    public static void setup(){
        dateStr = "16-05-2021";
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Test
    void shouldParseStringToDate(){
        Date parsedDate = CustomDateUtil.parseStringToDate(dateStr, dateFormat);
        String parsedDateStr = dateFormat.format(parsedDate);
        assertEquals(dateStr, parsedDateStr);
    }

    @Test
    void shouldConvertDateToUnixTimeStamp(){
        Date date = CustomDateUtil.parseStringToDate(dateStr, dateFormat);
        Long unixTimeStamp = CustomDateUtil.convertDateToUnixTimeStamp(date);
        assertEquals(1621123200, unixTimeStamp);
    }

    @Test
    void shouldNotConvertDateToUnixTimeStampWhenParameterIsNull(){
        GenBusinessException genBusinessException = assertThrows(GenBusinessException.class, () -> CustomDateUtil.convertDateToUnixTimeStamp(null));
        assertEquals(GenErrorMessage.DATE_COULD_NOT_BE_CONVERTED, genBusinessException.getBaseErrorMessage());
    }

    @Test
    void shouldGetLastWeek(){
        String lastWeek = CustomDateUtil.getLastWeek(dateFormat);
        assertEquals("30-06-2022", lastWeek);
    }

    @Test
    void shouldBetweenMinDateAndToday(){
        boolean betweenMinDateAndToday = CustomDateUtil.isBetweenMinDateAndToday(dateStr, CustomDateUtil.MIN_QUERYABLE_DATE, dateFormat);
        assertTrue(betweenMinDateAndToday);
    }
}

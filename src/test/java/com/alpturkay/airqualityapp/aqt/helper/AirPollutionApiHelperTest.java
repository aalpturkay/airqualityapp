package com.alpturkay.airqualityapp.aqt.helper;

import com.alpturkay.airqualityapp.aqt.dto.AirPollutionServiceResponseDto;
import com.alpturkay.airqualityapp.gen.config.AppConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.Mockito.*;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
public class AirPollutionApiHelperTest {

    @Autowired
    public AirPollutionApiHelper airPollutionApiHelper;



    static BigDecimal lat, lon;
    static Long unixStartDate, unixEndDate;

    @BeforeAll
    static void setup(){
        lat = new BigDecimal("39.9208");
        lon = new BigDecimal("32.8540");
        unixStartDate = 1621123200L;
        unixEndDate = 1621296000L;


    }

    @Test
    void shouldGetAirPollutionDataWhenReturnAirPollutionServiceResponseDto() {
        ReflectionTestUtils.setField(airPollutionApiHelper, "APP_ID", "9af83fb990b72f650afcba613cf44b7d");
        ReflectionTestUtils.setField(airPollutionApiHelper, "URL", "https://api.openweathermap.org/data/2.5/air_pollution/history");

        AirPollutionServiceResponseDto airPollutionServiceResponseDto = airPollutionApiHelper.
                getAirPollutionData(lat, lon, unixStartDate,unixEndDate);

        int actualSize = airPollutionServiceResponseDto.getList().size();
        assertEquals(49, actualSize);
    }
}
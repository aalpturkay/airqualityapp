package com.alpturkay.airqualityapp.aqt.service;

import com.alpturkay.airqualityapp.aqt.converter.AqtAirQualityConverter;
import com.alpturkay.airqualityapp.aqt.dao.AqtAirQualityDao;
import com.alpturkay.airqualityapp.aqt.helper.AQIClassifier;
import com.alpturkay.airqualityapp.aqt.helper.AirPollutionApiHelper;
import com.alpturkay.airqualityapp.aqt.helper.AirPollutionMathHelper;
import com.alpturkay.airqualityapp.cty.service.CtyCityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AqtAirQualityServiceTest {

    @Mock
    private AirPollutionApiHelper airPollutionApiHelper;

    @Mock
    private AirPollutionMathHelper airPollutionMathHelper;

    @Mock
    private AqtAirQualityDao aqtAirQualityDao;

    @Mock
    private CtyCityService ctyCityService;

    @Mock
    private AQIClassifier aqiClassifier;

    @Mock
    private AqtAirQualityConverter aqtAirQualityConverter;

    @InjectMocks
    private AqtAirQualityService aqtAirQualityService;

    @Test
    void shouldGetAirQuality(){
        
    }
}

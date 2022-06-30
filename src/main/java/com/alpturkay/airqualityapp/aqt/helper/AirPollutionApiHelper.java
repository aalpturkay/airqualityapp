package com.alpturkay.airqualityapp.aqt.helper;

import com.alpturkay.airqualityapp.aqt.dto.AirPollutionServiceResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
public class AirPollutionApiHelper {

    @Value("${weather.api.app.id}")
    private String APP_ID;

    @Value("${airpollution.api.url}")
    private String URL;

    public AirPollutionServiceResponseDto getAirPollutionData(BigDecimal lat, BigDecimal lon, Long unixStartDate, Long unixEndDate){

        RestTemplate restTemplate = new RestTemplate();


        ResponseEntity<AirPollutionServiceResponseDto> responseEntity = restTemplate.
                getForEntity(URL + "?lat={lat}&lon={lon}&start={start}&end={end}&appid={appid}",
                        AirPollutionServiceResponseDto.class, lat, lon, unixStartDate, unixEndDate, APP_ID);

        return responseEntity.getBody();
    }
}

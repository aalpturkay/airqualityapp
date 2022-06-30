package com.alpturkay.airqualityapp.cty.helper;

import com.alpturkay.airqualityapp.cty.dto.GeocodingServiceResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component("geocodingApiHelper")
public class GeocodingApiHelper {

    @Value("${weather.api.app.id}")
    private String APP_ID;

    @Value("${geocoding.api.url}")
    private String URL;

    public GeocodingServiceResponseDto getLocationByCityName(String cityName){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GeocodingServiceResponseDto[]> dtoResponseEntity = restTemplate
                .getForEntity(URL +"?q={q}&appid={appid}",
                        GeocodingServiceResponseDto[].class, cityName, APP_ID);

        return dtoResponseEntity.getBody()[0];
    }
}

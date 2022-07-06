package com.alpturkay.airqualityapp.cty.converter;

import com.alpturkay.airqualityapp.cty.dto.CtyCityDto;
import com.alpturkay.airqualityapp.cty.dto.GeocodingServiceResponseDto;
import com.alpturkay.airqualityapp.cty.entity.CtyCity;
import org.springframework.stereotype.Component;

@Component
public class CtyCityConverter {

    public CtyCityDto convertToCtyCityDto(CtyCity ctyCity){
        CtyCityDto ctyCityDto = new CtyCityDto();
        ctyCityDto.setCityName(ctyCity.getCityName());
        ctyCityDto.setLat(ctyCity.getLat());
        ctyCityDto.setLon(ctyCity.getLon());
        return ctyCityDto;
    }

    public CtyCity convertToCtyCity(String cityName, GeocodingServiceResponseDto geocodingServiceResponseDto){
        CtyCity ctyCity = new CtyCity();
        ctyCity.setCityName(cityName);
        ctyCity.setLat(geocodingServiceResponseDto.getLat());
        ctyCity.setLon(geocodingServiceResponseDto.getLon());
        return ctyCity;
    }
}

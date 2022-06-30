package com.alpturkay.airqualityapp.cty.service;

import com.alpturkay.airqualityapp.cty.dto.CtyCitySaveRequestDto;
import com.alpturkay.airqualityapp.cty.dto.GeocodingServiceResponseDto;
import com.alpturkay.airqualityapp.cty.entity.CtyCity;
import com.alpturkay.airqualityapp.cty.helper.GeocodingApiHelper;
import com.alpturkay.airqualityapp.gen.enums.GenErrorMessage;
import com.alpturkay.airqualityapp.gen.exceptions.ItemDuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CtyCityService {
    private final CtyCityEntityService ctyCityEntityService;
    private final GeocodingApiHelper geocodingApiHelper;

    public CtyCity save(CtyCitySaveRequestDto ctyCitySaveRequestDto){

        String cityName = ctyCitySaveRequestDto.getCityName();
        cityName = StringUtils.capitalize(cityName.toLowerCase());

        boolean existsByCityName = ctyCityEntityService.existsByCityName(cityName);

        if (existsByCityName){
            throw new ItemDuplicateException(GenErrorMessage.ITEM_ALREADY_EXISTS);
        }

        GeocodingServiceResponseDto location = geocodingApiHelper.getLocationByCityName(cityName);

        CtyCity ctyCity = new CtyCity();

        ctyCity.setCityName(cityName);
        ctyCity.setLat(location.getLat());
        ctyCity.setLon(location.getLon());

        //aqtAirQualityService.getAirQualityData("Ankara", "20-06-2022", "22-06-2022");

        return ctyCityEntityService.save(ctyCity);
    }



}

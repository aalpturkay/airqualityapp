package com.alpturkay.airqualityapp.cty.service;

import com.alpturkay.airqualityapp.aqt.service.AqtAirQualityService;
import com.alpturkay.airqualityapp.cty.dto.CtyCitySaveRequestDto;
import com.alpturkay.airqualityapp.cty.dto.GeocodingServiceResponseDto;
import com.alpturkay.airqualityapp.cty.entity.CtyCity;
import com.alpturkay.airqualityapp.cty.helper.GeocodingApiHelper;
import com.alpturkay.airqualityapp.gen.enums.GenErrorMessage;
import com.alpturkay.airqualityapp.gen.exceptions.ItemDuplicateException;
import com.alpturkay.airqualityapp.gen.utils.CustomStringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CtyCityService {
    private final CtyCityEntityService ctyCityEntityService;
    private final GeocodingApiHelper geocodingApiHelper;
    private AqtAirQualityService aqtAirQualityService;

    @Autowired
    public void setAqtAirQualityService(@Lazy AqtAirQualityService aqtAirQualityService){
        this.aqtAirQualityService = aqtAirQualityService;
    }

    public CtyCity save(CtyCitySaveRequestDto ctyCitySaveRequestDto){

        String cityName = ctyCitySaveRequestDto.getCityName();
        cityName = CustomStringUtil.lowerAndCapitalizeFirstLetter(cityName);

        boolean existsByCityName = existsByCityName(cityName);

        if (existsByCityName){
            throw new ItemDuplicateException(GenErrorMessage.ITEM_ALREADY_EXISTS);
        }

        GeocodingServiceResponseDto location = geocodingApiHelper.getLocationByCityName(cityName);

        CtyCity ctyCity = new CtyCity();

        ctyCity.setCityName(cityName);
        ctyCity.setLat(location.getLat());
        ctyCity.setLon(location.getLon());


        return ctyCityEntityService.save(ctyCity);
    }

    public void getAirQualityData(){
        //aqtAirQualityService.getAirQualityData("Ankara", "20-06-2022", "22-06-2022");
    }

    public boolean existsByCityName(String cityName) {
        return ctyCityEntityService.existsByCityName(cityName);
    }

    public CtyCity findByCityName(String cityName) {
        return ctyCityEntityService.findByCityName(cityName);
    }
}

package com.alpturkay.airqualityapp.cty.service;

import com.alpturkay.airqualityapp.cty.converter.CtyCityConverter;
import com.alpturkay.airqualityapp.cty.dto.CtyCityDto;
import com.alpturkay.airqualityapp.cty.dto.CtyCitySaveRequestDto;
import com.alpturkay.airqualityapp.cty.dto.GeocodingServiceResponseDto;
import com.alpturkay.airqualityapp.cty.entity.CtyCity;
import com.alpturkay.airqualityapp.cty.helper.GeocodingApiHelper;
import com.alpturkay.airqualityapp.gen.enums.GenErrorMessage;
import com.alpturkay.airqualityapp.gen.exceptions.ItemDuplicateException;
import com.alpturkay.airqualityapp.gen.utils.CustomStringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CtyCityService {
    private final CtyCityEntityService ctyCityEntityService;
    private final GeocodingApiHelper geocodingApiHelper;
    private final CtyCityConverter ctyCityConverter;

    public CtyCityDto save(CtyCitySaveRequestDto ctyCitySaveRequestDto){

        String cityName = ctyCitySaveRequestDto.getCityName();
        cityName = CustomStringUtil.lowerAndCapitalizeFirstLetter(cityName);

        boolean existsByCityName = existsByCityName(cityName);

        if (existsByCityName){
            throw new ItemDuplicateException(GenErrorMessage.ITEM_ALREADY_EXISTS);
        }

        GeocodingServiceResponseDto geocodingServiceResponseDto = geocodingApiHelper.getLocationByCityName(cityName);

        CtyCity ctyCity = ctyCityConverter.convertToCtyCity(cityName, geocodingServiceResponseDto);



        ctyCity = ctyCityEntityService.save(ctyCity);

        CtyCityDto ctyCityDto = ctyCityConverter.convertToCtyCityDto(ctyCity);
        return ctyCityDto;
    }

    public boolean existsByCityName(String cityName) {
        return ctyCityEntityService.existsByCityName(cityName);
    }

    public CtyCity findByCityName(String cityName) {
        return ctyCityEntityService.findByCityName(cityName);
    }
}

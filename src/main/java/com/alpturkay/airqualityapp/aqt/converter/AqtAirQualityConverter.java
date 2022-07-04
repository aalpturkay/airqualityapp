package com.alpturkay.airqualityapp.aqt.converter;

import com.alpturkay.airqualityapp.aqt.dto.*;
import com.alpturkay.airqualityapp.aqt.entity.AqtAirQuality;
import com.alpturkay.airqualityapp.aqt.enums.EnumAqtAirQualityCategoryType;
import com.alpturkay.airqualityapp.cty.entity.CtyCity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AqtAirQualityConverter {

    public AqtAirQualityResultDto convertToAqtAirQualityResultDto(AqtAirQuality aqtAirQuality){
        AqtAirQualityResultDto aqtAirQualityResultDto = new AqtAirQualityResultDto();
        List<AqtAirQualityCategoryDto> aqtAirQualityCategoryDtoList = new ArrayList<>();

        AqtPollutantSO2Dto aqtPollutantSO2Dto = convertToAqtPollutantSO2Dto(aqtAirQuality.getCategorySO2());
        AqtPollutantO3Dto aqtPollutantO3Dto = convertToAqtPollutantO3Dto(aqtAirQuality.getCategoryO3());
        AqtPollutantCODto aqtPollutantCODto = convertToAqtPollutantCODto(aqtAirQuality.getCategoryCO());

        aqtAirQualityCategoryDtoList.add(aqtPollutantSO2Dto);
        aqtAirQualityCategoryDtoList.add(aqtPollutantO3Dto);
        aqtAirQualityCategoryDtoList.add(aqtPollutantCODto);

        aqtAirQualityResultDto.setDate(aqtAirQuality.getDate());
        aqtAirQualityResultDto.setCategories(aqtAirQualityCategoryDtoList);

        return aqtAirQualityResultDto;
    }

    public AqtAirQuality convertToAqtAirQuality(CtyCity ctyCity, String date,
                                                EnumAqtAirQualityCategoryType categoryTypeCO,
                                                EnumAqtAirQualityCategoryType categoryTypeSO2,
                                                EnumAqtAirQualityCategoryType categoryTypeO3){
        AqtAirQuality aqtAirQuality = new AqtAirQuality();
        aqtAirQuality.setCtyCity(ctyCity);
        aqtAirQuality.setDate(date);
        aqtAirQuality.setCategoryCO(categoryTypeCO);
        aqtAirQuality.setCategorySO2(categoryTypeSO2);
        aqtAirQuality.setCategoryO3(categoryTypeO3);
        return aqtAirQuality;
    }

    private AqtPollutantCODto convertToAqtPollutantCODto(EnumAqtAirQualityCategoryType categoryTypeCO){
        AqtPollutantCODto aqtPollutantCODto = new AqtPollutantCODto();
        aqtPollutantCODto.setCo(categoryTypeCO.getReadableName());
        return aqtPollutantCODto;
    }

    private AqtPollutantSO2Dto convertToAqtPollutantSO2Dto(EnumAqtAirQualityCategoryType categoryTypeSO2){
        AqtPollutantSO2Dto aqtPollutantSO2Dto = new AqtPollutantSO2Dto();
        aqtPollutantSO2Dto.setSo2(categoryTypeSO2.getReadableName());
        return aqtPollutantSO2Dto;
    }

    private AqtPollutantO3Dto convertToAqtPollutantO3Dto(EnumAqtAirQualityCategoryType categoryTypeO3){
        AqtPollutantO3Dto aqtPollutantO3Dto = new AqtPollutantO3Dto();
        aqtPollutantO3Dto.setO3(categoryTypeO3.getReadableName());
        return aqtPollutantO3Dto;
    }


}

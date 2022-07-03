package com.alpturkay.airqualityapp.aqt.dto;

import com.alpturkay.airqualityapp.aqt.entity.AqtAirQualityCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AqtPollutantSO2Dto extends AqtAirQualityCategoryDto {
    @JsonProperty("SO2")
    private String so2;
}

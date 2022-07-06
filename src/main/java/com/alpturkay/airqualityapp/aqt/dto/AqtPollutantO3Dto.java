package com.alpturkay.airqualityapp.aqt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AqtPollutantO3Dto extends AqtAirQualityCategoryDto {
    @JsonProperty("O3")
    private String o3;
}

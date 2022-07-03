package com.alpturkay.airqualityapp.aqt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AqtPollutantCODto extends AqtAirQualityCategoryDto {
    @JsonProperty("CO")
    private String co;
}

package com.alpturkay.airqualityapp.aqt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AqtAirQualityCategoryDto {
    @JsonProperty("CO")
    private String co;

    @JsonProperty("SO2")
    private String so2;

    @JsonProperty("O3")
    private String o3;
}

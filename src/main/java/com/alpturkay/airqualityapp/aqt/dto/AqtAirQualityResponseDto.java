package com.alpturkay.airqualityapp.aqt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AqtAirQualityResponseDto {
    @JsonProperty("City")
    private String city;

    @JsonProperty("Results")
    private List<AqtAirQualityResultDto> results;

}

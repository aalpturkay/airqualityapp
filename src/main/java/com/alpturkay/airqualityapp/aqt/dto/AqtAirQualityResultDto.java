package com.alpturkay.airqualityapp.aqt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AqtAirQualityResultDto {
    @JsonProperty("Date")
    private String date;

    @JsonProperty("Categories")
    private List<AqtAirQualityCategoryDto> categories;
}

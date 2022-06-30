package com.alpturkay.airqualityapp.cty.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GeocodingServiceResponseDto {
    private BigDecimal lat;
    private BigDecimal lon;
}

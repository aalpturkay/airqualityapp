package com.alpturkay.airqualityapp.aqt.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AirPollutionServiceResponseListItemComponentsDto {
    private BigDecimal co;
    private BigDecimal o3;
    private BigDecimal so2;
}

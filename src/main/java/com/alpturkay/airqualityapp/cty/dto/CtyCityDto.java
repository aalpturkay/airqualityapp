package com.alpturkay.airqualityapp.cty.dto;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
public class CtyCityDto {
    private String cityName;

    private BigDecimal lat;

    private BigDecimal lon;
}

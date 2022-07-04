package com.alpturkay.airqualityapp.aqt.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pollutant {
    private BigDecimal co;
    private BigDecimal so2;
    private BigDecimal o3;
}

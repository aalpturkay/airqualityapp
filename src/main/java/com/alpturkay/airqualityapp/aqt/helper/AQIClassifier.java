package com.alpturkay.airqualityapp.aqt.helper;

import com.alpturkay.airqualityapp.aqt.enums.EnumAqtAirQualityCategoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class AQIClassifier {

    private int goodIndex;
    private int satisfactoryIndex;
    private int moderateIndex;
    private int poorIndex;
    private int severeIndex;
    private int hazardousIndex;

    public EnumAqtAirQualityCategoryType classify(final BigDecimal pollutantAverage){
        EnumAqtAirQualityCategoryType category = EnumAqtAirQualityCategoryType.NONE;
        if (pollutantAverage.compareTo(BigDecimal.valueOf(goodIndex)) < 0){
            category = EnumAqtAirQualityCategoryType.GOOD;
        } else if (pollutantAverage.compareTo(BigDecimal.valueOf(goodIndex+1)) > 0 && pollutantAverage.compareTo(BigDecimal.valueOf(satisfactoryIndex)) < 0){
            category = EnumAqtAirQualityCategoryType.SATISFACTORY;
        } else if (pollutantAverage.compareTo(BigDecimal.valueOf(satisfactoryIndex+1)) > 0 && pollutantAverage.compareTo(BigDecimal.valueOf(moderateIndex)) < 0){
            category = EnumAqtAirQualityCategoryType.MODERATE;
        } else if (pollutantAverage.compareTo(BigDecimal.valueOf(moderateIndex+1)) > 0 && pollutantAverage.compareTo(BigDecimal.valueOf(poorIndex)) < 0){
            category = EnumAqtAirQualityCategoryType.POOR;
        } else if (pollutantAverage.compareTo(BigDecimal.valueOf(poorIndex+1)) > 0 && pollutantAverage.compareTo(BigDecimal.valueOf(severeIndex)) < 0){
            category = EnumAqtAirQualityCategoryType.SEVERE;
        } else if (pollutantAverage.compareTo(BigDecimal.valueOf(hazardousIndex)) > 0){
            category = EnumAqtAirQualityCategoryType.HAZARDOUS;
        }
        return category;
    }
}

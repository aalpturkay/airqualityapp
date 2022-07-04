package com.alpturkay.airqualityapp.aqt.helper;

import com.alpturkay.airqualityapp.aqt.enums.EnumAqtAirQualityCategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class AQIClassifier {

    public EnumAqtAirQualityCategoryType classify(final AQIValues aqiValues ,final BigDecimal pollutantAverage){
        EnumAqtAirQualityCategoryType category = EnumAqtAirQualityCategoryType.NONE;
        if (pollutantAverage.compareTo(BigDecimal.valueOf(aqiValues.getGoodIndex())) < 0){
            category = EnumAqtAirQualityCategoryType.GOOD;
        } else if (pollutantAverage.compareTo(BigDecimal.valueOf(aqiValues.getGoodIndex()+1)) > 0 && pollutantAverage.compareTo(BigDecimal.valueOf(aqiValues.getSatisfactoryIndex())) < 0){
            category = EnumAqtAirQualityCategoryType.SATISFACTORY;
        } else if (pollutantAverage.compareTo(BigDecimal.valueOf(aqiValues.getSatisfactoryIndex()+1)) > 0 && pollutantAverage.compareTo(BigDecimal.valueOf(aqiValues.getModerateIndex())) < 0){
            category = EnumAqtAirQualityCategoryType.MODERATE;
        } else if (pollutantAverage.compareTo(BigDecimal.valueOf(aqiValues.getModerateIndex()+1)) > 0 && pollutantAverage.compareTo(BigDecimal.valueOf(aqiValues.getPoorIndex())) < 0){
            category = EnumAqtAirQualityCategoryType.POOR;
        } else if (pollutantAverage.compareTo(BigDecimal.valueOf(aqiValues.getPoorIndex()+1)) > 0 && pollutantAverage.compareTo(BigDecimal.valueOf(aqiValues.getSevereIndex())) < 0){
            category = EnumAqtAirQualityCategoryType.SEVERE;
        } else if (pollutantAverage.compareTo(BigDecimal.valueOf(aqiValues.getHazardousIndex())) > 0){
            category = EnumAqtAirQualityCategoryType.HAZARDOUS;
        }
        return category;
    }
}

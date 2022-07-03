package com.alpturkay.airqualityapp.aqt.enums;

import lombok.ToString;

public enum EnumAqtAirQualityCategoryType {
    GOOD("Good"),
    SATISFACTORY("Satisfactory"),
    MODERATE("Moderate"),
    POOR("Poor"),
    SEVERE("Severe"),
    HAZARDOUS("Hazardous"),
    NONE("None")
    ;

    private final String readableName;

    EnumAqtAirQualityCategoryType(String readableName) {
        this.readableName = readableName;
    }

    public String getReadableName(){
        return readableName;
    }

}

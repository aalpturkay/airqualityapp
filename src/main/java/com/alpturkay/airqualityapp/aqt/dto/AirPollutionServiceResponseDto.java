package com.alpturkay.airqualityapp.aqt.dto;

import lombok.Data;

import java.util.List;

@Data
public class AirPollutionServiceResponseDto {
    private List<AirPollutionServiceResponseListItemDto> list;
}

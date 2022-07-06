package com.alpturkay.airqualityapp.aqt.controller;

import com.alpturkay.airqualityapp.aqt.dto.AqtAirQualityResponseDto;
import com.alpturkay.airqualityapp.aqt.service.AqtAirQualityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequestMapping("/air_quality")
@Slf4j
@RequiredArgsConstructor
public class AqtAirQualityController {

    private final AqtAirQualityService aqtAirQualityService;

    @GetMapping
    public ResponseEntity<AqtAirQualityResponseDto> getAirQuality(@RequestParam String city,
                                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Optional<String> startDate,
                                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Optional<String> endDate){

        return ResponseEntity.ok(aqtAirQualityService.getAirQuality(city, startDate, endDate));
    }
}

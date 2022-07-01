package com.alpturkay.airqualityapp.aqt.controller;

import com.alpturkay.airqualityapp.aqt.service.AqtAirQualityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Date;

@RestController
@RequestMapping("/air_quality")
@Slf4j
@RequiredArgsConstructor
public class AqtAirQualityController {

    private final AqtAirQualityService aqtAirQualityService;

    @GetMapping
    public void getAirQuality(@RequestParam String city,
                              @RequestParam("startDate") @DateTimeFormat(pattern = "dd-MM-yyyy") String startDate,
                              @RequestParam("endDate") @DateTimeFormat(pattern = "dd-MM-yyyy") String endDate){

        log.info("{} - {} - {}", city, startDate, endDate);
        aqtAirQualityService.getAirQualityData(city, startDate, endDate);
    }
}

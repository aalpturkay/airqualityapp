package com.alpturkay.airqualityapp.aqt.service;


import com.alpturkay.airqualityapp.aqt.dto.AirPollutionServiceResponseDto;
import com.alpturkay.airqualityapp.aqt.helper.AirPollutionApiHelper;
import com.alpturkay.airqualityapp.cty.entity.CtyCity;
import com.alpturkay.airqualityapp.cty.service.CtyCityService;
import com.alpturkay.airqualityapp.gen.utils.CustomDateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AqtAirQualityService {

    private final AirPollutionApiHelper airPollutionApiHelper;
    private CtyCityService ctyCityService;

    @Autowired
    public void setCtyCityService(CtyCityService ctyCityService){
        this.ctyCityService = ctyCityService;
    }

    public void getAirQualityData(String cityName, String startDate, String endDate){

        CtyCity allowedCity = getAllowedCityOrThrow(cityName);

        // convert string to date -> iterate over dates -> get pollution data for each date

        Date start;
        Date end;
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        List<AirPollutionServiceResponseDto> airPollutionList = new ArrayList<>();


        try {
            start = dateFormat.parse(startDate);
            end = dateFormat.parse(endDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        startCal.setTime(start);
        endCal.setTime(end);

        for (Date date = startCal.getTime(); startCal.before(endCal); startCal.add(Calendar.DATE, 1), date = startCal.getTime()) {
            log.info("DATE: {}", date);
            //Long unixTimeStamp = CustomDateUtil.convertDateToUnixTimeStamp(date);
            //AirPollutionServiceResponseDto airPollutionData = airPollutionApiHelper.getAirPollutionData(allowedCity.getLat(), allowedCity.getLon(), unixTimeStamp);
            //airPollutionList.add(airPollutionData);
        }
        
        // Long unixStartTime = CustomDateUtil.convertDateToUnixTimeStamp(start);
        log.info("AIR POLLUTION LIST LEN: {}", airPollutionList.size());
        airPollutionList.stream().map(data -> data.getList().get(0).getComponents().getCo()).forEach(co -> log.info("CO: {}", co));

        /*
        AirPollutionServiceResponseDto airPollutionData = airPollutionApiHelper
                .getAirPollutionData(allowedCity.getLat(), allowedCity.getLon(), unixStartTime);


        log.info("AIR POLLUTION SO2 DATA: {}", airPollutionData.getList().get(0).getComponents().getSo2());
        */
    }

    // Todo: make this exception custom.
    private CtyCity getAllowedCityOrThrow(String cityName) {
        /*
        if(!ctyCityService.existsByCityName(cityName))
            throw new RuntimeException("You are not allowed to query for this city.");
         */
        boolean isExists = ctyCityService.existsByCityName(cityName);
        return ctyCityService.findByCityName(cityName);
    }



}

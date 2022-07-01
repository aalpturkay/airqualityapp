package com.alpturkay.airqualityapp.aqt.service;


import com.alpturkay.airqualityapp.aqt.dao.AqtAirQualityDao;
import com.alpturkay.airqualityapp.aqt.dao.AqtAirQualityResultDao;
import com.alpturkay.airqualityapp.aqt.dto.AirPollutionServiceResponseDto;
import com.alpturkay.airqualityapp.aqt.entity.AqtAirQualityResult;
import com.alpturkay.airqualityapp.aqt.helper.AirPollutionApiHelper;
import com.alpturkay.airqualityapp.cty.entity.CtyCity;
import com.alpturkay.airqualityapp.cty.service.CtyCityService;
import com.alpturkay.airqualityapp.gen.utils.CustomDateUtil;
import com.alpturkay.airqualityapp.gen.utils.CustomStringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AqtAirQualityService {

    private final AirPollutionApiHelper airPollutionApiHelper;
    private CtyCityService ctyCityService;
    private final AqtAirQualityResultDao aqtAirQualityResultDao;
    private final AqtAirQualityDao aqtAirQualityDao;

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
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));


        List<AirPollutionServiceResponseDto> airPollutionList = new ArrayList<>();

        try {
            start = dateFormat.parse(startDate);
            end = dateFormat.parse(endDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        startCal.setTime(start);
        endCal.setTime(end);
        endCal.add(Calendar.DATE, 1);

        AirPollutionServiceResponseDto airPollutionData = airPollutionApiHelper.getAirPollutionData(allowedCity.getLat(), allowedCity.getLon(),
                CustomDateUtil.convertDateToUnixTimeStamp(startCal.getTime()),
                CustomDateUtil.convertDateToUnixTimeStamp(endCal.getTime())
        );
        log.info("List Size: {}", airPollutionData.getList().size());
        // Classify air quality (Good etc.)

        // Todo: Add null check
        List<AqtAirQualityResult> aqtAirQualityResults = aqtAirQualityResultDao.
                findByDateAndAqtAirQuality(startDate, aqtAirQualityDao.findByCity(cityName).getId());


        /*
        for (Date date = startCal.getTime(); startCal.before(endCal); startCal.add(Calendar.DATE, 1), date = startCal.getTime()) {
            log.info("DATE: {}", date);
            Long unixTimeStamp = CustomDateUtil.convertDateToUnixTimeStamp(date);
            log.info("DATE UNIX: {}", unixTimeStamp);

            //AirPollutionServiceResponseDto airPollutionData = airPollutionApiHelper.getAirPollutionData(allowedCity.getLat(), allowedCity.getLon(), unixTimeStamp);
            //airPollutionList.add(airPollutionData);
        }
         */

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
        cityName = CustomStringUtil.lowerAndCapitalizeFirstLetter(cityName);
        if(!ctyCityService.existsByCityName(cityName))
            throw new RuntimeException("You are not allowed to query for this city.");

        return ctyCityService.findByCityName(cityName);
    }



}

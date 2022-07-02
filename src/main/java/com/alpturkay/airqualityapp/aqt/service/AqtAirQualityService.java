package com.alpturkay.airqualityapp.aqt.service;


import com.alpturkay.airqualityapp.aqt.dao.AqtAirQualityDao;
import com.alpturkay.airqualityapp.aqt.dao.AqtAirQualityResultDao;
import com.alpturkay.airqualityapp.aqt.dto.AirPollutionServiceResponseDto;
import com.alpturkay.airqualityapp.aqt.dto.AirPollutionServiceResponseListItemComponentsDto;
import com.alpturkay.airqualityapp.aqt.dto.AirPollutionServiceResponseListItemDto;
import com.alpturkay.airqualityapp.aqt.entity.AqtAirQuality;
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

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        final int DAY = 24;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));


        List<AirPollutionServiceResponseDto> airPollutionList = new ArrayList<>();
        Queue<List<AirPollutionServiceResponseListItemComponentsDto>> pollutants = new LinkedList<>();

        try {
            start = dateFormat.parse(startDate);
            end = dateFormat.parse(endDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        startCal.setTime(start);
        endCal.setTime(end);
        endCal.add(Calendar.DATE, 1);

        /*
        AirPollutionServiceResponseDto airPollutionData = airPollutionApiHelper.
                getAirPollutionData(allowedCity.getLat(), allowedCity.getLon(),
                CustomDateUtil.convertDateToUnixTimeStamp(startCal.getTime()),
                CustomDateUtil.convertDateToUnixTimeStamp(endCal.getTime())
        );
        log.info("List Size: {}", airPollutionData.getList().size());
         */
        // Classify air quality (Good etc.)
        if (aqtAirQualityDao.existsByCtyCity(allowedCity)){
            for (Date date = startCal.getTime(); startCal.before(endCal); startCal.add(Calendar.DATE, 1), date = startCal.getTime()) {

                aqtAirQualityResultDao.findByDateAndAqtAirQuality(dateFormat.format(date), 1L);
            }
        } else {
            Long startUnixTimeStamp = CustomDateUtil.convertDateToUnixTimeStamp(startCal.getTime());
            Long endUnixTimeStamp = CustomDateUtil.convertDateToUnixTimeStamp(endCal.getTime());
            BigDecimal sumOfCO = new BigDecimal(0);
            BigDecimal meanOfCO;
            AirPollutionServiceResponseDto airPollutionData = airPollutionApiHelper.getAirPollutionData(allowedCity.getLat(), allowedCity.getLon(),
                    startUnixTimeStamp, endUnixTimeStamp);
            for (int i = 0; i < airPollutionData.getList().size() / DAY; i++) {
                List<AirPollutionServiceResponseListItemDto> subList = airPollutionData.getList().subList(i * DAY, (i + 1) * DAY);
                //subList.stream().map(data -> data.getComponents()).reduce(0, (integer, airPollutionServiceResponseListItemComponentsDto) -> integer.getCo().add());

            }
            airPollutionList.add(airPollutionData);
        }



        // If the query is in the DB
        /*
        if (aqtAirQualityDao.existsByCity(cityName)){
            AqtAirQuality aqtAirQuality = aqtAirQualityDao.findByCity(cityName);
            Long aqtAirQualityId = aqtAirQuality.getId();
            //List<AqtAirQualityResult> airQualityResultList = aqtAirQualityResultDao.findByDateAndAqtAirQuality(dateFormat.format(start), aqtAirQualityId);
        } else {
           log.info("Data will request from the api");
        }
         */


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

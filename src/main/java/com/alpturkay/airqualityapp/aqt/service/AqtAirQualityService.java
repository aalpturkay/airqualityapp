package com.alpturkay.airqualityapp.aqt.service;


import com.alpturkay.airqualityapp.aqt.dao.AqtAirQualityDao;
import com.alpturkay.airqualityapp.aqt.dao.AqtAirQualityResultDao;
import com.alpturkay.airqualityapp.aqt.dto.*;
import com.alpturkay.airqualityapp.aqt.entity.AqtAirQuality;
import com.alpturkay.airqualityapp.aqt.enums.EnumAqtAirQualityCategoryType;
import com.alpturkay.airqualityapp.aqt.helper.AQIClassifier;
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
import java.math.RoundingMode;
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

    public AqtAirQualityResponseDto getAirQualityData(String cityName, String startDate, String endDate){

        CtyCity allowedCity = getAllowedCityOrThrow(cityName);

        AqtAirQualityResponseDto aqtAirQualityResponseDto = new AqtAirQualityResponseDto();
        List<AqtAirQualityResultDto> aqtAirQualityResultDtoList = new ArrayList<>();
        AqtAirQualityCategoryDto aqtAirQualityCategoryDto = new AqtAirQualityCategoryDto();

        aqtAirQualityResponseDto.setCity(allowedCity.getCityName());

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

            AirPollutionServiceResponseDto airPollutionData = airPollutionApiHelper.getAirPollutionData(allowedCity.getLat(), allowedCity.getLon(),
                    startUnixTimeStamp, endUnixTimeStamp);
            int i = 0;
            for (Date date = startCal.getTime(); startCal.before(endCal); startCal.add(Calendar.DATE, 1), date = startCal.getTime()) {
                BigDecimal sumOfCO = new BigDecimal(0);
                BigDecimal sumOfSO2 = new BigDecimal(0);
                BigDecimal sumOfO3 = new BigDecimal(0);
                BigDecimal averageOfCO = new BigDecimal(0);
                BigDecimal averageOfO3 = new BigDecimal(0);
                BigDecimal averageOfSO2 = new BigDecimal(0);
                List<AirPollutionServiceResponseListItemDto> subList = airPollutionData.getList().subList(i * DAY, (i + 1) * DAY);

                for (int j = 0; j < subList.size(); j++) {
                    AirPollutionServiceResponseListItemDto itemDto = subList.get(j);
                    BigDecimal co = itemDto.getComponents().getCo();
                    BigDecimal o3 = itemDto.getComponents().getO3();
                    BigDecimal so2 = itemDto.getComponents().getSo2();
                    sumOfCO = sumOfCO.add(co);
                    sumOfSO2 = sumOfSO2.add(so2);
                    sumOfO3 = sumOfO3.add(o3);
                }
                averageOfCO = sumOfCO.divide(new BigDecimal(DAY), 2, RoundingMode.HALF_DOWN);
                averageOfSO2 = sumOfSO2.divide(new BigDecimal(DAY), 2, RoundingMode.HALF_DOWN);
                averageOfO3 = sumOfO3.divide(new BigDecimal(DAY), 2, RoundingMode.HALF_DOWN);

                AQIClassifier aqiClassifierSO2 = new AQIClassifier(40, 80,
                        380, 800, 1600, 1600);

                AQIClassifier aqiClassifierCO = new AQIClassifier(50, 100,
                        150, 200, 300, 300);

                AQIClassifier aqiClassifierO3 = new AQIClassifier(50, 100,
                        168, 208, 748, 748);

                EnumAqtAirQualityCategoryType categorySO2 = aqiClassifierSO2.classify(averageOfSO2);
                EnumAqtAirQualityCategoryType categoryCO = aqiClassifierCO.classify(averageOfCO);
                EnumAqtAirQualityCategoryType categoryO3 = aqiClassifierO3.classify(averageOfO3);

                // save to the db

                AqtAirQuality aqtAirQuality = new AqtAirQuality();
                aqtAirQuality.setCtyCity(allowedCity);
                aqtAirQuality.setDate(dateFormat.format(date));
                aqtAirQuality.setCategoryCO(categoryCO);
                aqtAirQuality.setCategoryO3(categoryO3);
                aqtAirQuality.setCategorySO2(categorySO2);

                aqtAirQualityDao.save(aqtAirQuality);

                // Dto converting

                AqtPollutantCODto aqtPollutantCODto = new AqtPollutantCODto();
                AqtPollutantO3Dto aqtPollutantO3Dto = new AqtPollutantO3Dto();
                AqtPollutantSO2Dto aqtPollutantSO2Dto = new AqtPollutantSO2Dto();

                aqtPollutantCODto.setCo(categoryCO.getReadableName());
                aqtPollutantO3Dto.setO3(categoryO3.getReadableName());
                aqtPollutantSO2Dto.setSo2(categorySO2.getReadableName());

                List<AqtAirQualityCategoryDto> aqtAirQualityCategoryDtoList = new ArrayList<>();
                aqtAirQualityCategoryDtoList.add(aqtPollutantCODto);
                aqtAirQualityCategoryDtoList.add(aqtPollutantO3Dto);
                aqtAirQualityCategoryDtoList.add(aqtPollutantSO2Dto);

                AqtAirQualityResultDto aqtAirQualityResultDto = new AqtAirQualityResultDto();
                aqtAirQualityResultDto.setDate(dateFormat.format(date));
                aqtAirQualityResultDto.setCategories(aqtAirQualityCategoryDtoList);

                aqtAirQualityResultDtoList.add(aqtAirQualityResultDto);

                i++;
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
        aqtAirQualityResponseDto.setResults(aqtAirQualityResultDtoList);

        return aqtAirQualityResponseDto;
    }

    // Todo: make this exception custom.
    private CtyCity getAllowedCityOrThrow(String cityName) {
        cityName = CustomStringUtil.lowerAndCapitalizeFirstLetter(cityName);
        if(!ctyCityService.existsByCityName(cityName))
            throw new RuntimeException("You are not allowed to query for this city.");

        return ctyCityService.findByCityName(cityName);
    }



}

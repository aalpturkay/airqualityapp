package com.alpturkay.airqualityapp.aqt.service;


import com.alpturkay.airqualityapp.aqt.converter.AqtAirQualityConverter;
import com.alpturkay.airqualityapp.aqt.dao.AqtAirQualityDao;
import com.alpturkay.airqualityapp.aqt.dto.*;
import com.alpturkay.airqualityapp.aqt.entity.AqtAirQuality;
import com.alpturkay.airqualityapp.aqt.enums.EnumAqtAirQualityCategoryType;
import com.alpturkay.airqualityapp.aqt.helper.*;
import com.alpturkay.airqualityapp.cty.entity.CtyCity;
import com.alpturkay.airqualityapp.cty.service.CtyCityService;
import com.alpturkay.airqualityapp.gen.utils.CustomDateUtil;
import com.alpturkay.airqualityapp.gen.utils.CustomStringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final AirPollutionMathHelper airPollutionMathHelper;
    private final CtyCityService ctyCityService;
    private final AqtAirQualityDao aqtAirQualityDao;
    private final AqtAirQualityConverter aqtAirQualityConverter;
    private final AQIClassifier aqiClassifier;

    public AqtAirQualityResponseDto getAirQualityData(String cityName, String startDate, String endDate){

        CtyCity allowedCity = getAllowedCityWithControl(cityName);

        AqtAirQualityResponseDto aqtAirQualityResponseDto = new AqtAirQualityResponseDto();
        List<AqtAirQualityResultDto> aqtAirQualityResultDtoList = new ArrayList<>();
        aqtAirQualityResponseDto.setCity(allowedCity.getCityName());

        // convert string to date -> iterate over dates -> get pollution data for each date

        final int DAY = 24;
        Date start, end;
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            start = dateFormat.parse(startDate);
            end = dateFormat.parse(endDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        startCal.setTime(start);
        endCal.setTime(end);
        endCal.add(Calendar.DATE, 1);

        // Classify air quality (Good etc.)
        if (aqtAirQualityDao.existsByCtyCity(allowedCity)){


            for (Date date = startCal.getTime(); startCal.before(endCal); startCal.add(Calendar.DATE, 1), date = startCal.getTime()) {
                List<String> datesInDB = new ArrayList<>();
                List<String> datesNotInDB = new ArrayList<>();
                Optional<AqtAirQuality> optionalAqtAirQuality = aqtAirQualityDao.findByCtyCityAndDate(allowedCity, dateFormat.format(date));
                if (optionalAqtAirQuality.isPresent()){
                    datesInDB.add(dateFormat.format(date));
                } else{
                    datesNotInDB.add(dateFormat.format(date));
                }
                if (!datesInDB.isEmpty())
                    log.info("{}, {} ile {} -> veri tabanından alınacak.", allowedCity.getCityName(), datesInDB.get(0), datesInDB.get(datesInDB.size() - 1));

                if (!datesNotInDB.isEmpty())
                    log.info("{}, {} ile {} -> API'den alınacak.", allowedCity.getCityName(), datesNotInDB.get(0), datesNotInDB.get(datesInDB.size() - 1));


            }


        } else {
            Long startUnixTimeStamp = CustomDateUtil.convertDateToUnixTimeStamp(startCal.getTime());
            Long endUnixTimeStamp = CustomDateUtil.convertDateToUnixTimeStamp(endCal.getTime());

            AirPollutionServiceResponseDto airPollutionData = airPollutionApiHelper.getAirPollutionData(allowedCity.getLat(), allowedCity.getLon(),
                    startUnixTimeStamp, endUnixTimeStamp);
            int i = 0;
            for (Date date = startCal.getTime(); startCal.before(endCal); startCal.add(Calendar.DATE, 1), date = startCal.getTime()) {
                BigDecimal sumOfSO2 = new BigDecimal(0);
                BigDecimal sumOfO3 = new BigDecimal(0);
                BigDecimal sumOfCO = new BigDecimal(0);

                List<AirPollutionServiceResponseListItemDto> subList = airPollutionData.getList().subList(i * DAY, (i + 1) * DAY);

                Pollutant averagesOfPollutants = airPollutionMathHelper.
                        getAveragesOfPollutants(subList, new Pollutant(sumOfCO, sumOfSO2, sumOfO3), DAY);


                EnumAqtAirQualityCategoryType categorySO2 = aqiClassifier.classify(new AQIValues(40, 80,
                        380, 800, 1600, 1600), averagesOfPollutants.getSo2());
                EnumAqtAirQualityCategoryType categoryCO = aqiClassifier.classify(new AQIValues(50, 100,
                        150, 200, 300, 300), averagesOfPollutants.getCo());
                EnumAqtAirQualityCategoryType categoryO3 = aqiClassifier.classify(new AQIValues(50, 100,
                        168, 208, 748, 748), averagesOfPollutants.getO3());

                // save to the db
                AqtAirQuality aqtAirQuality = aqtAirQualityConverter.convertToAqtAirQuality(allowedCity, dateFormat.format(date),
                        categoryCO, categorySO2, categoryO3);

                aqtAirQuality = aqtAirQualityDao.save(aqtAirQuality);

                // Dto converting
                AqtAirQualityResultDto aqtAirQualityResultDto = aqtAirQualityConverter.
                        convertToAqtAirQualityResultDto(aqtAirQuality);

                aqtAirQualityResultDtoList.add(aqtAirQualityResultDto);

                i++;
            }
        }

        aqtAirQualityResponseDto.setResults(aqtAirQualityResultDtoList);

        return aqtAirQualityResponseDto;
    }

    // Todo: make this exception custom.
    private CtyCity getAllowedCityWithControl(String cityName) {
        cityName = CustomStringUtil.lowerAndCapitalizeFirstLetter(cityName);
        if(!ctyCityService.existsByCityName(cityName))
            throw new RuntimeException("You are not allowed to query for this city.");

        return ctyCityService.findByCityName(cityName);
    }



}

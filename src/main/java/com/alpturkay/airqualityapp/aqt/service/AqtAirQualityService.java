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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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

    public AqtAirQualityResponseDto getAirQualityData(String cityName, Optional<String> startDateOptional, Optional<String> endDateOptional){

        CtyCity allowedCity = getAllowedCityWithControl(cityName);

        AqtAirQualityResponseDto aqtAirQualityResponseDto = new AqtAirQualityResponseDto();
        List<AqtAirQualityResultDto> aqtAirQualityResultDtoList = new ArrayList<>();
        aqtAirQualityResponseDto.setCity(allowedCity.getCityName());

        // convert string to date -> iterate over dates -> get pollution data for each date

        final int DAY = 24;
        Date start, end;
        AtomicReference<String> startDate = new AtomicReference<>();
        AtomicReference<String> endDate = new AtomicReference<>();
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));


        startDateOptional.ifPresentOrElse(startDate::set, () ->
                startDate.set(CustomDateUtil.getLastWeek(dateFormat)));

        endDateOptional.ifPresentOrElse(endDate::set, () ->
                endDate.set(dateFormat.format(startCal.getTime())));

        try {
            start = dateFormat.parse(startDate.get());
            end = dateFormat.parse(endDate.get());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        startCal.setTime(start);
        endCal.setTime(end);
        endCal.add(Calendar.DATE, 1);

        // Classify air quality (Good etc.)
        if (aqtAirQualityDao.existsByCtyCity(allowedCity)){
            List<String> dates = new ArrayList<>();
            List<String> datesInDB = new ArrayList<>();
            List<String> datesNotInDB = new ArrayList<>();
            Date myDate;

            for (Date date = startCal.getTime(); startCal.before(endCal); startCal.add(Calendar.DATE, 1), date = startCal.getTime())
                dates.add(dateFormat.format(date));

            // 15, 16, 17, 18, 19
            int datesSize = dates.size();
            int i = 0;
            while (i < dates.size()) {
                boolean isInDB = aqtAirQualityDao.existsByCtyCityAndDate(allowedCity, dates.get(i));
                if (isInDB){
                    AqtAirQuality aqtAirQuality = aqtAirQualityDao.findByCtyCityAndDate(allowedCity, dates.get(i));
                    AqtAirQualityResultDto aqtAirQualityResultDto = aqtAirQualityConverter.
                            convertToAqtAirQualityResultDto(aqtAirQuality);
                    aqtAirQualityResultDtoList.add(aqtAirQualityResultDto);

                    datesInDB.add(dates.get(i));
                    if (datesSize - 1 == i){
                        log.info("{} arası DB'den", datesInDB.get(datesInDB.size() - 1));
                    }
                    else if (!aqtAirQualityDao.existsByCtyCityAndDate(allowedCity, dates.get(i+1)))
                        log.info("{} - {} arası DB'den", datesInDB.get(0), datesInDB.get(datesInDB.size() - 1));
                }else{
                    datesNotInDB.add(dates.get(i));
                    if (datesSize - 1 == i){
                        saveAirQualityAndSetResultDto(allowedCity, aqtAirQualityResultDtoList, DAY,
                                datesNotInDB.get(datesNotInDB.size() - 1), datesNotInDB.get(datesNotInDB.size() - 1), dateFormat);
                        log.info("{} API'den", datesNotInDB.get(datesNotInDB.size() - 1));
                        datesNotInDB.clear();
                    }
                    else if (aqtAirQualityDao.existsByCtyCityAndDate(allowedCity, dates.get(i+1))) {
                        saveAirQualityAndSetResultDto(allowedCity, aqtAirQualityResultDtoList, DAY,
                                datesNotInDB.get(0), datesNotInDB.get(datesNotInDB.size() - 1), dateFormat);
                        log.info("{} - {} arası API'den", datesNotInDB.get(0), datesNotInDB.get(datesNotInDB.size() - 1));
                        datesNotInDB.clear();
                    }
                }
                i++;
            }
        } else {
            // Todo: add log
            saveAirQualityAndSetResultDto(allowedCity, aqtAirQualityResultDtoList, DAY, startDate.get(), endDate.get(), dateFormat);
        }

        aqtAirQualityResponseDto.setResults(aqtAirQualityResultDtoList);

        return aqtAirQualityResponseDto;
    }

    private void saveAirQualityAndSetResultDto(CtyCity allowedCity, List<AqtAirQualityResultDto> aqtAirQualityResultDtoList, int DAY, String startDate, String endDate, SimpleDateFormat dateFormat) {
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        Date start, end;

        try {
            start = dateFormat.parse(startDate);
            end = dateFormat.parse(endDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        startCal.setTime(start);
        endCal.setTime(end);
        endCal.add(Calendar.DATE, 1);
        Long startDateUnix = CustomDateUtil.convertDateToUnixTimeStamp(startCal.getTime());
        Long endDateUnix = CustomDateUtil.convertDateToUnixTimeStamp(endCal.getTime());

        AirPollutionServiceResponseDto airPollutionData = airPollutionApiHelper.getAirPollutionData(allowedCity.getLat(),
                allowedCity.getLon(), startDateUnix, endDateUnix);

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

    private void saveAirQualityAndSetResultDtoOld(CtyCity allowedCity, List<AqtAirQualityResultDto> aqtAirQualityResultDtoList, int DAY, Calendar startCal, Calendar endCal, SimpleDateFormat dateFormat, AirPollutionServiceResponseDto airPollutionData) {
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
    
    // Todo: make this exception custom.
    private CtyCity getAllowedCityWithControl(String cityName) {
        cityName = CustomStringUtil.lowerAndCapitalizeFirstLetter(cityName);
        if(!ctyCityService.existsByCityName(cityName))
            throw new RuntimeException("You are not allowed to query for this city.");

        return ctyCityService.findByCityName(cityName);
    }



}

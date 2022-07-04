package com.alpturkay.airqualityapp.aqt.helper;

import com.alpturkay.airqualityapp.aqt.dto.AirPollutionServiceResponseListItemDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class AirPollutionMathHelper {

    public Pollutant getAveragesOfPollutants(final List<AirPollutionServiceResponseListItemDto> subList,
                                              Pollutant sumOfPollutants, final int DAY){

        BigDecimal day = new BigDecimal(DAY);
        BigDecimal sumOfCO = sumOfPollutants.getCo();
        BigDecimal sumOfSO2 = sumOfPollutants.getSo2();
        BigDecimal sumOfO3 = sumOfPollutants.getO3();

        for (int j = 0; j < subList.size(); j++) {
            AirPollutionServiceResponseListItemDto itemDto = subList.get(j);
            BigDecimal co = itemDto.getComponents().getCo();
            BigDecimal o3 = itemDto.getComponents().getO3();
            BigDecimal so2 = itemDto.getComponents().getSo2();
            sumOfCO = sumOfCO.add(co);
            sumOfSO2 = sumOfSO2.add(so2);
            sumOfO3 = sumOfO3.add(o3);
        }

        Pollutant averagesOfPollutants = new Pollutant();

        averagesOfPollutants.setCo(sumOfCO.divide(day, 2, RoundingMode.HALF_DOWN));
        averagesOfPollutants.setSo2(sumOfSO2.divide(day, 2, RoundingMode.HALF_DOWN));
        averagesOfPollutants.setO3(sumOfO3.divide(day, 2, RoundingMode.HALF_DOWN));

        return averagesOfPollutants;
    }
}

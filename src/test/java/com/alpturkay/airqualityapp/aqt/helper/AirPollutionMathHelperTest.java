package com.alpturkay.airqualityapp.aqt.helper;

import com.alpturkay.airqualityapp.aqt.dto.AirPollutionServiceResponseListItemComponentsDto;
import com.alpturkay.airqualityapp.aqt.dto.AirPollutionServiceResponseListItemDto;
import com.alpturkay.airqualityapp.gen.config.AppConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
public class AirPollutionMathHelperTest {

    @Autowired
    public AirPollutionMathHelper airPollutionMathHelper;

    static List<AirPollutionServiceResponseListItemDto> subList;
    static AirPollutionServiceResponseListItemDto listItemDto;
    static AirPollutionServiceResponseListItemComponentsDto componentsDto;
    static BigDecimal sumOfSO2;
    static BigDecimal sumOfO3;
    static BigDecimal sumOfCO;
    @BeforeAll
    static void setup(){
        subList = new ArrayList<>();
        listItemDto = new AirPollutionServiceResponseListItemDto();
        componentsDto = new AirPollutionServiceResponseListItemComponentsDto();

        sumOfCO = new BigDecimal(0);
        sumOfO3 = new BigDecimal(0);
        sumOfSO2 = new BigDecimal(0);

        for (int i = 0; i < 24; i++) {
            componentsDto.setCo(BigDecimal.ONE);
            componentsDto.setO3(BigDecimal.ONE);
            componentsDto.setSo2(BigDecimal.ONE);
            listItemDto.setComponents(componentsDto);
            subList.add(listItemDto);
        }
    }

    @Test
    void shouldGetAveragesOfPollutants() {

        Pollutant have = airPollutionMathHelper.getAveragesOfPollutants(subList,
                new Pollutant(sumOfCO, sumOfSO2, sumOfO3), 24);

        Pollutant expected = new Pollutant(new BigDecimal("1.00"), new BigDecimal("1.00"), new BigDecimal("1.00"));

        assertEquals(expected, have);
    }
}
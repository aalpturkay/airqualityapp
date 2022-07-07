package com.alpturkay.airqualityapp.gen.config;

import com.alpturkay.airqualityapp.aqt.helper.AirPollutionApiHelper;
import com.alpturkay.airqualityapp.aqt.helper.AirPollutionMathHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public AirPollutionApiHelper airPollutionApiHelper(){
        return new AirPollutionApiHelper();
    }

    @Bean
    public AirPollutionMathHelper airPollutionMathHelper(){
        return new AirPollutionMathHelper();
    }
}

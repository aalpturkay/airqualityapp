package com.alpturkay.airqualityapp.aqt.dao;

import com.alpturkay.airqualityapp.aqt.entity.AqtAirQuality;
import com.alpturkay.airqualityapp.cty.entity.CtyCity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AqtAirQualityDao extends JpaRepository<AqtAirQuality, Long> {
    AqtAirQuality findByCtyCityAndDate(CtyCity city, String date);
    boolean existsByCtyCityAndDate(CtyCity city, String date);
    boolean existsByCtyCity(CtyCity city);
}

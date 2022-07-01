package com.alpturkay.airqualityapp.aqt.dao;

import com.alpturkay.airqualityapp.aqt.entity.AqtAirQuality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AqtAirQualityDao extends JpaRepository<AqtAirQuality, Long> {
    AqtAirQuality findByCity(String city);
}

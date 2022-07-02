package com.alpturkay.airqualityapp.aqt.dao;

import com.alpturkay.airqualityapp.aqt.entity.AqtAirQualityResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AqtAirQualityResultDao extends JpaRepository<AqtAirQualityResult, Long> {
    AqtAirQualityResult findByDateAndAqtAirQuality(String date, Long aqtAirQuality);
    //boolean existsByDateAndAirQuality(String date, Long aqtAirQuality);
}

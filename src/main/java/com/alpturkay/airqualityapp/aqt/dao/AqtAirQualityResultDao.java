package com.alpturkay.airqualityapp.aqt.dao;

import com.alpturkay.airqualityapp.aqt.entity.AqtAirQualityResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AqtAirQualityResultDao extends JpaRepository<AqtAirQualityResult, Long> {
    List<AqtAirQualityResult> findByDateAndAqtAirQuality(String date, Long aqtAirQuality);
}

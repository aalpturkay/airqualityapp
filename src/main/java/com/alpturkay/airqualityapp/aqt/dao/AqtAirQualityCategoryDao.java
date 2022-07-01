package com.alpturkay.airqualityapp.aqt.dao;

import com.alpturkay.airqualityapp.aqt.entity.AqtAirQualityCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AqtAirQualityCategoryDao extends JpaRepository<AqtAirQualityCategory, Long> {
}

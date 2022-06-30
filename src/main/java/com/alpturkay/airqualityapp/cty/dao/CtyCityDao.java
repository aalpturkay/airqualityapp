package com.alpturkay.airqualityapp.cty.dao;

import com.alpturkay.airqualityapp.cty.entity.CtyCity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CtyCityDao extends JpaRepository<CtyCity, Long> {
    boolean existsByCityName(String cityName);
    CtyCity findByCityName(String cityName);
}

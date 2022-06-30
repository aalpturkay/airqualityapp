package com.alpturkay.airqualityapp.aqt.entity;

import com.alpturkay.airqualityapp.aqt.enums.EnumAqtAirQualityCategoryType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "AQT_AIR_QUALITY_CATEGORY")
@Getter
@Setter
public class AqtAirQualityCategory {
    @Id
    @SequenceGenerator(name = "AqtAirQualityCategory", sequenceName = "AQT_AIR_QUALITY_CATEGORY_ID_SEQ")
    @GeneratedValue(generator = "AqtAirQualityCategory")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "POLLUTANT")
    private EnumAqtAirQualityCategoryType pollutant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AQT_AIR_QUALITY_RESULT")
    private AqtAirQualityResult aqtAirQualityResult;
}

package com.alpturkay.airqualityapp.aqt.entity;

import com.alpturkay.airqualityapp.aqt.enums.EnumAqtAirQualityCategoryType;
import com.alpturkay.airqualityapp.cty.entity.CtyCity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;


@Entity
@Table(name = "AQT_AIR_QUALITY")
@Getter
@Setter
public class AqtAirQuality {
    @Id
    @SequenceGenerator(name = "AqtAirQuality", sequenceName = "AQT_AIR_QUALITY_ID_SEQ")
    @GeneratedValue(generator = "AqtAirQuality")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CTY_CITY")
    private CtyCity ctyCity;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String date;

    @Enumerated(EnumType.STRING)
    @Column(name = "CATEGORY_CO")
    private EnumAqtAirQualityCategoryType categoryCO;

    @Enumerated(EnumType.STRING)
    @Column(name = "CATEGORY_SO2")
    private EnumAqtAirQualityCategoryType categorySO2;

    @Enumerated(EnumType.STRING)
    @Column(name = "CATEGORY_O3")
    private EnumAqtAirQualityCategoryType categoryO3;

}

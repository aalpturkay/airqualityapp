package com.alpturkay.airqualityapp.aqt.entity;

import com.alpturkay.airqualityapp.cty.entity.CtyCity;
import lombok.Getter;
import lombok.Setter;

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
}

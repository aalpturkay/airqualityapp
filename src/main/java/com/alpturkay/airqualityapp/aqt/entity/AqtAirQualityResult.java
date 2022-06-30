package com.alpturkay.airqualityapp.aqt.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AQT_AIR_QUALITY_RESULT")
@Getter
@Setter
public class AqtAirQualityResult {
    @Id
    @SequenceGenerator(name = "AqtAirQualityResult", sequenceName = "AQT_AIR_QUALITY_RESULT_ID_SEQ")
    @GeneratedValue(generator = "AqtAirQualityResult")
    private Long id;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AQT_AIR_QUALITY")
    private AqtAirQuality aqtAirQuality;
}

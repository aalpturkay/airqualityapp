package com.alpturkay.airqualityapp.cty.entity;

import com.alpturkay.airqualityapp.gen.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "CTY_CITY")
public class CtyCity extends BaseEntity {
    @Id
    @SequenceGenerator(name = "CtyCity", sequenceName = "CTY_CITY_ID_SEQ")
    @GeneratedValue(generator = "CtyCity")
    private Long id;

    @Column(name = "CITY_NAME")
    private String cityName;

    @Column(name = "LAT", precision = 18, scale = 4)
    private BigDecimal lat;

    @Column(name = "LON", precision = 19, scale = 4)
    private BigDecimal lon;
}

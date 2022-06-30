package com.alpturkay.airqualityapp.gen.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity implements BaseModel, Serializable, Cloneable {
    @Embedded
    private BaseAdditionalFields baseAdditionalFields;
}

package com.alpturkay.airqualityapp.gen.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Date;

@Embeddable
@Getter
@Setter
public class BaseAdditionalFields {
    @Column(name = "CREATED_DATE", updatable = false)
    @CreatedDate
    private Date createdDate;

    @Column(name = "UPDATED_DATE")
    @LastModifiedDate
    private Date updatedDate;
}

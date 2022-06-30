package com.alpturkay.airqualityapp.gen.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class GenExceptionResponse {
    private Date errDate;
    private String message;
    private String detail;
}

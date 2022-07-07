package com.alpturkay.airqualityapp.gen.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DateNotValidException extends GenBusinessException{
    public DateNotValidException(BaseErrorMessage baseErrorMessage) {
        super(baseErrorMessage);
    }
}
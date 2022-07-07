package com.alpturkay.airqualityapp.gen.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CityNotAllowedException extends GenBusinessException{
    public CityNotAllowedException(BaseErrorMessage baseErrorMessage) {
        super(baseErrorMessage);
    }
}

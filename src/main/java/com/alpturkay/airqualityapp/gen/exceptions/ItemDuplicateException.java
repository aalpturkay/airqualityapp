package com.alpturkay.airqualityapp.gen.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ItemDuplicateException extends GenBusinessException{
    public ItemDuplicateException(BaseErrorMessage baseErrorMessage) {
        super(baseErrorMessage);
    }
}

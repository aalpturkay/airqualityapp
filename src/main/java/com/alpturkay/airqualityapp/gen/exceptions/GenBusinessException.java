package com.alpturkay.airqualityapp.gen.exceptions;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@RequiredArgsConstructor
public class GenBusinessException extends RuntimeException{
    private final BaseErrorMessage baseErrorMessage;
}

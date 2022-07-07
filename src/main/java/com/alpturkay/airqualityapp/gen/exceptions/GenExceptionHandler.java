package com.alpturkay.airqualityapp.gen.exceptions;

import com.alpturkay.airqualityapp.gen.response.GenExceptionResponse;
import com.alpturkay.airqualityapp.gen.response.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GenExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleGenBusinessExceptions(GenBusinessException e, WebRequest webRequest){
        Date errDate = new Date();
        String message = e.getBaseErrorMessage().getMessage();
        String description = webRequest.getDescription(false);

        return getResponseEntity(errDate, message, description, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleItemDuplicateException(ItemDuplicateException e, WebRequest webRequest){
        Date errDate = new Date();
        String message = e.getBaseErrorMessage().getMessage();
        String description = webRequest.getDescription(false);

        return getResponseEntity(errDate, message, description, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleDateNotValidException(DateNotValidException e, WebRequest webRequest){
        Date errDate = new Date();
        String message = e.getBaseErrorMessage().getMessage();
        String description = webRequest.getDescription(false);
        GenExceptionResponse genExceptionResponse = new GenExceptionResponse(errDate, message, description);

        return new ResponseEntity<>(genExceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleCityNotAllowedException(CityNotAllowedException e, WebRequest webRequest){
        Date errDate = new Date();
        String message = e.getBaseErrorMessage().getMessage();
        String description = webRequest.getDescription(false);
        GenExceptionResponse genExceptionResponse = new GenExceptionResponse(errDate, message, description);

        return new ResponseEntity<>(genExceptionResponse, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> getResponseEntity(Date errDate, String message, String description, HttpStatus httpStatus) {
        GenExceptionResponse genExceptionResponse = new GenExceptionResponse(errDate, message, description);

        RestResponse<GenExceptionResponse> restResponse = RestResponse.error(genExceptionResponse);
        restResponse.setMessage(message);

        return new ResponseEntity<>(restResponse, httpStatus);
    }
}

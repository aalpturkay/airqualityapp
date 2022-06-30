package com.alpturkay.airqualityapp.gen.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestResponse<T> implements Serializable {

    private T data;
    private Date responseDate;
    private boolean success;
    private String message;

    public RestResponse(T data, boolean success) {
        this.data = data;
        this.success = success;
        responseDate = new Date();
    }

    public static <T> RestResponse<T> of(T t){
        return new RestResponse<>(t, true);
    }

    public static <T> RestResponse<T> error(T t){
        return new RestResponse<>(t, false);
    }

    public static <T> RestResponse<T> empty(){
        return new RestResponse<>(null, true);
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

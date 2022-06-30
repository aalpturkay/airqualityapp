package com.alpturkay.airqualityapp.gen.enums;


import com.alpturkay.airqualityapp.gen.exceptions.BaseErrorMessage;

public enum GenErrorMessage implements BaseErrorMessage {
    ITEM_ALREADY_EXISTS("This item is already exists"),
    ;

    private String message;

    GenErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}

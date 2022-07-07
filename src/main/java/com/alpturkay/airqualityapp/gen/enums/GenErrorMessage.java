package com.alpturkay.airqualityapp.gen.enums;


import com.alpturkay.airqualityapp.gen.exceptions.BaseErrorMessage;

public enum GenErrorMessage implements BaseErrorMessage {
    ITEM_ALREADY_EXISTS("This item is already exists!"),
    DATE_COULD_NOT_BE_CONVERTED("Date can't be null!"),
    DATE_COULD_NOT_BE_PARSED("Date could not be parsed!"),
    DATE_MUST_BE_BETWEEN_RESTRICTIONS("Date must be between 27-11-2020 and today!"),
    CITY_NOT_ALLOWED("You are not allowed to query for this city!"),
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

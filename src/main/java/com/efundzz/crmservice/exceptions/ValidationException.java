package com.efundzz.crmservice.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class ValidationException extends RuntimeException {

    private final Map<String, String> fieldErrors;
    public ValidationException(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

}

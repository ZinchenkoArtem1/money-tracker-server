package com.zinchenko.common.error;

import org.springframework.http.HttpStatus;

public class GenericException extends RuntimeException {

    private final HttpStatus httpStatus;

    public GenericException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public GenericException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

package com.samsoft.lms.core.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiException extends Exception {

    private HttpStatus httpStatus;

    public ApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}

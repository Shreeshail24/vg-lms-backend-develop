package com.samsoft.lms.core.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidInputException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	private final HttpStatus httpCode;

	public InvalidInputException() {
		super();
		this.httpCode = null;

	}

	public InvalidInputException(String message, HttpStatus httpStatusCode) {
		super(message);
		this.httpCode = httpStatusCode;
	}

	public final HttpStatus getHttpCode() {
		return httpCode;
	}

}
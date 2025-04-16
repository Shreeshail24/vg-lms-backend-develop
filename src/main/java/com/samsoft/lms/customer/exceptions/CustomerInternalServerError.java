package com.samsoft.lms.customer.exceptions;

public class CustomerInternalServerError extends RuntimeException{
	private static final long serial1VersionUID = 1L;

	public CustomerInternalServerError(String message) {
		super(message);
	}

}
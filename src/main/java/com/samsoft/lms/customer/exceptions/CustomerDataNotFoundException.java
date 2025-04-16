package com.samsoft.lms.customer.exceptions;

public class CustomerDataNotFoundException extends RuntimeException{
	private static final long serial1VersionUID = 1L;

	public CustomerDataNotFoundException(String message) {
		super(message);
	}

}
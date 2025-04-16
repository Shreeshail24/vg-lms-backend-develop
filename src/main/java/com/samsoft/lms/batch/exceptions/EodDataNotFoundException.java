package com.samsoft.lms.batch.exceptions;

public class EodDataNotFoundException extends RuntimeException{
	private static final long serial1VersionUID = 1L;

	public EodDataNotFoundException(String message) {
		super(message);
	}

}
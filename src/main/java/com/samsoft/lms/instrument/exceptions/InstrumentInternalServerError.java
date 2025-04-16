package com.samsoft.lms.instrument.exceptions;

public class InstrumentInternalServerError extends RuntimeException{
	private static final long serial1VersionUID = 1L;

	public InstrumentInternalServerError(String message) {
		super(message);
	}

}
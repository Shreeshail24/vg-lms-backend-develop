package com.samsoft.lms.core.exceptions;

public class CoreInternalServerError extends RuntimeException{
	private static final long serial1VersionUID = 1L;

	public CoreInternalServerError(String message) {
		super(message);
	}

}
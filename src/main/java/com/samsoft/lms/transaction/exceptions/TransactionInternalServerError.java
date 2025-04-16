package com.samsoft.lms.transaction.exceptions;

public class TransactionInternalServerError extends RuntimeException{
	private static final long serial1VersionUID = 1L;

	public TransactionInternalServerError(String message) {
		super(message);
	}

}
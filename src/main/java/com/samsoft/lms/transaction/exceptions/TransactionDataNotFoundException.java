package com.samsoft.lms.transaction.exceptions;

public class TransactionDataNotFoundException extends RuntimeException{
	private static final long serial1VersionUID = 1L;

	public TransactionDataNotFoundException(String message) {
		super(message);
	}

}
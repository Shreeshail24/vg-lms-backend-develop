package com.samsoft.lms.batch.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EodBadRequestException extends RuntimeException{
	private static final long serial1VersionUID = 1L;

	public EodBadRequestException(String message) {
		super(message);
		log.info(message);
	}

}
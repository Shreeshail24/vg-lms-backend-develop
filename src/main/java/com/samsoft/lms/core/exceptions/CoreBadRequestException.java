package com.samsoft.lms.core.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CoreBadRequestException extends RuntimeException{
	private static final long serial1VersionUID = 1L;

	public CoreBadRequestException(String message) {
		super(message);
		log.info(message);
	}

}
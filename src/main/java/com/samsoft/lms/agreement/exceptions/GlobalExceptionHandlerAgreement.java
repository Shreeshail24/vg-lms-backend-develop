package com.samsoft.lms.agreement.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.samsoft.lms.agreement.dto.AgreementResponseDto;
import com.samsoft.lms.core.dto.CoreResponseDto;

@ControllerAdvice
public class GlobalExceptionHandlerAgreement {

	@ExceptionHandler(AgreementDataNotFoundException.class)
	public ResponseEntity<AgreementResponseDto> agrDateNotFound(AgreementDataNotFoundException ex) {
		AgreementResponseDto response = new AgreementResponseDto();
		response.setResponseCode(HttpStatus.NOT_FOUND.value());
		response.setResponseMessage(ex.getMessage());
		return new ResponseEntity<AgreementResponseDto>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AgreementInternalServerError.class)
	public ResponseEntity<AgreementResponseDto> agrInternalSeverError(AgreementInternalServerError ex) {
		AgreementResponseDto response = new AgreementResponseDto();
		response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.setResponseMessage(ex.getMessage());
		return new ResponseEntity<AgreementResponseDto>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(AgreementBadRequestException.class)
	public ResponseEntity<AgreementResponseDto> agrBadRequestException(AgreementBadRequestException ex) {
		AgreementResponseDto response = new AgreementResponseDto();
		response.setResponseCode(HttpStatus.BAD_REQUEST.value());
		response.setResponseMessage(ex.getMessage());
		return new ResponseEntity<AgreementResponseDto>(response, HttpStatus.BAD_REQUEST);
	}
}

package com.samsoft.lms.batch.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.samsoft.lms.batch.dto.EodResponseDto;

@ControllerAdvice
public class GlobalExceptionHandlerBatch {
	
	@ExceptionHandler(EodDataNotFoundException.class)
	public ResponseEntity<EodResponseDto> coreDataNotFound(EodDataNotFoundException ex){
		EodResponseDto response = new EodResponseDto();
        response.setResponseCode(HttpStatus.NOT_FOUND.value());
        response.setResponseMessage(ex.getMessage()); 
        return new ResponseEntity<EodResponseDto>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(EodInternalServerError.class)
	public ResponseEntity<EodResponseDto> coreInternalSeverError(EodInternalServerError ex){
		EodResponseDto response = new EodResponseDto();
        response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setResponseMessage(ex.getMessage());
        return new ResponseEntity<EodResponseDto>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(EodBadRequestException.class)
	public ResponseEntity<EodResponseDto> coreBadRequest(EodBadRequestException ex){
		EodResponseDto response = new EodResponseDto();
        response.setResponseCode(HttpStatus.BAD_REQUEST.value());
        response.setResponseMessage(ex.getMessage());
        return new ResponseEntity<EodResponseDto>(response, HttpStatus.BAD_REQUEST);
	}
}


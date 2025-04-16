package com.samsoft.lms.instrument.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.samsoft.lms.customer.dto.CustomerResponseDto;
import com.samsoft.lms.instrument.dto.InstrumentResponseDto;

@ControllerAdvice
public class GlobalExceptionHandlerInstrument {
	
	@ExceptionHandler(InstrumentDataNotFoundException.class)
	public ResponseEntity<InstrumentResponseDto> instDataNotFound(InstrumentDataNotFoundException ex){
		InstrumentResponseDto response = new InstrumentResponseDto();
        response.setResponseCode(HttpStatus.NOT_FOUND.value());
        response.setResponseMessage(ex.getMessage()); 
        return new ResponseEntity<InstrumentResponseDto>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InstrumentInternalServerError.class)
	public ResponseEntity<InstrumentResponseDto> instInternalSeverError(InstrumentInternalServerError ex){
		InstrumentResponseDto response = new InstrumentResponseDto();
        response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setResponseMessage(ex.getMessage());
        return new ResponseEntity<InstrumentResponseDto>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}


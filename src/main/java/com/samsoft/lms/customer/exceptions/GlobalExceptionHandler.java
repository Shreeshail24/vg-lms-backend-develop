package com.samsoft.lms.customer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.samsoft.lms.customer.dto.CustomerResponseDto;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(CustomerDataNotFoundException.class)
	public ResponseEntity<CustomerResponseDto> customerDataNotFound(CustomerDataNotFoundException ex){
		CustomerResponseDto response = new CustomerResponseDto();
        response.setResponseCode(HttpStatus.NOT_FOUND.value());
        response.setResponseMessage(ex.getMessage()); 
        return new ResponseEntity<CustomerResponseDto>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(CustomerInternalServerError.class)
	public ResponseEntity<CustomerResponseDto> customerInternalSeverError(CustomerInternalServerError ex){
		CustomerResponseDto response = new CustomerResponseDto();
        response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setResponseMessage(ex.getMessage());
        return new ResponseEntity<CustomerResponseDto>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}


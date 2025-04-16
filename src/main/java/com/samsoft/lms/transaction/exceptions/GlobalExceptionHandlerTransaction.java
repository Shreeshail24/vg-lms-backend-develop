package com.samsoft.lms.transaction.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.samsoft.lms.transaction.dto.TransactionResponseDto;

@ControllerAdvice
public class GlobalExceptionHandlerTransaction {
	
	@ExceptionHandler(TransactionDataNotFoundException.class)
	public ResponseEntity<TransactionResponseDto> transactionDataNotFound(TransactionDataNotFoundException ex){
		TransactionResponseDto response = new TransactionResponseDto();
        response.setResponseCode(HttpStatus.NOT_FOUND.value());
        response.setResponseMessage(ex.getMessage()); 
        return new ResponseEntity<TransactionResponseDto>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(TransactionInternalServerError.class)
	public ResponseEntity<TransactionResponseDto> transactionInternalSeverError(TransactionInternalServerError ex){
		TransactionResponseDto response = new TransactionResponseDto();
        response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setResponseMessage(ex.getMessage());
        return new ResponseEntity<TransactionResponseDto>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}


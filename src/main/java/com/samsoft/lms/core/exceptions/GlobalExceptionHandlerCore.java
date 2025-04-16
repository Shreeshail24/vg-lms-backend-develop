package com.samsoft.lms.core.exceptions;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.samsoft.lms.core.dto.CoreResponseDto;
import com.samsoft.lms.core.exceptions.dto.ApiError;
import com.samsoft.lms.core.exceptions.dto.ResponseEntityBuilder;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandlerCore extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(CoreDataNotFoundException.class)
	public ResponseEntity<CoreResponseDto> coreDataNotFound(CoreDataNotFoundException ex){
		CoreResponseDto response = new CoreResponseDto();
        response.setResponseCode(HttpStatus.NOT_FOUND.value());
        response.setResponseMessage(ex.getMessage()); 
        return new ResponseEntity<CoreResponseDto>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(CoreInternalServerError.class)
	public ResponseEntity<CoreResponseDto> coreInternalSeverError(CoreInternalServerError ex){
		CoreResponseDto response = new CoreResponseDto();
        response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setResponseMessage(ex.getMessage());
        return new ResponseEntity<CoreResponseDto>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(CoreBadRequestException.class)
	public ResponseEntity<CoreResponseDto> coreBadRequest(CoreBadRequestException ex){
		CoreResponseDto response = new CoreResponseDto();
        response.setResponseCode(HttpStatus.BAD_REQUEST.value());
        response.setResponseMessage(ex.getMessage());
        return new ResponseEntity<CoreResponseDto>(response, HttpStatus.BAD_REQUEST);
	}

	//
	// handleHttpMediaTypeNotSupported : triggers when the JSON is invalid
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, String> errors = new HashMap<>();

		StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

		errors.put("message", builder.toString());


		ApiError<Map<String, String>> apiError = new ApiError<>();
		apiError.setHttpStatus(HttpStatus.BAD_REQUEST);
		apiError.setResponseCode(HttpStatus.BAD_REQUEST.value());
		apiError.setResponseMessage("Invalid JSON");
		apiError.setErrors(errors);

		return ResponseEntityBuilder.build(apiError);
	}

	// handleHttpMessageNotReadable : triggers when the JSON is malformed
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, String> errors = new HashMap<>();
		errors.put("message", ex.getMessage());

		ApiError<Map<String, String>> apiError = new ApiError<>();
		apiError.setHttpStatus(HttpStatus.BAD_REQUEST);
		apiError.setResponseCode(HttpStatus.BAD_REQUEST.value());
		apiError.setResponseMessage("Malformed JSON request");
		apiError.setErrors(errors);

		return ResponseEntityBuilder.build(apiError);
	}

	// handleMethodArgumentNotValid : triggers when @Valid fails
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<Map<String, String>> errorList = new ArrayList<>();

		ex.getBindingResult().getAllErrors().forEach((error) -> {
			Map<String, String> errorMap = new HashMap<>();
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errorMap.put("fieldName", fieldName);
			errorMap.put("message", errorMessage);
			errorList.add(errorMap);
		});

		ApiError<List<Map<String, String>>> apiError = new ApiError<>();
		apiError.setHttpStatus(HttpStatus.BAD_REQUEST);
		apiError.setResponseCode(HttpStatus.BAD_REQUEST.value());
		apiError.setResponseMessage("Validation Errors");
		apiError.setErrors(errorList);

		return ResponseEntityBuilder.build(apiError);
	}

	// handleMissingServletRequestParameter : triggers when there are missing parameters
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, String> errors = new HashMap<>();
		errors.put("message", ex.getParameterName() + " parameter is missing");

		ApiError<Map<String, String>> apiError = new ApiError<>();
		apiError.setHttpStatus(HttpStatus.BAD_REQUEST);
		apiError.setResponseCode(HttpStatus.BAD_REQUEST.value());
		apiError.setResponseMessage("Missing Parameters");
		apiError.setErrors(errors);

		return ResponseEntityBuilder.build(apiError);
	}

	// handleNoHandlerFoundException : triggers when the handler method is invalid
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, String> errors = new HashMap<>();
		errors.put("message", String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));

		ApiError<Map<String, String>> apiError = new ApiError<>();
		apiError.setHttpStatus(HttpStatus.BAD_REQUEST);
		apiError.setResponseCode(HttpStatus.BAD_REQUEST.value());
		apiError.setResponseMessage("Method Not Found");
		apiError.setErrors(errors);

		return ResponseEntityBuilder.build(apiError);
	}

	// handleTypeMismatch : triggers when a parameter's type does not match
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, String> errors = new HashMap<>();
		errors.put("message", ex.getMessage());

		ApiError<Map<String, String>> apiError = new ApiError<>();
		apiError.setHttpStatus(HttpStatus.BAD_REQUEST);
		apiError.setResponseCode(HttpStatus.BAD_REQUEST.value());
		apiError.setResponseMessage("Mismatch Type");
		apiError.setErrors(errors);

		return ResponseEntityBuilder.build(apiError);
	}

	// handleMethodArgumentTypeMismatch : triggers when a parameter's type does not match
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
																	  WebRequest request) {
		Map<String, String> errors = new HashMap<>();
		errors.put("message", ex.getMessage());

		ApiError<Map<String, String>> apiError = new ApiError<>();
		apiError.setHttpStatus(HttpStatus.BAD_REQUEST);
		apiError.setResponseCode(HttpStatus.BAD_REQUEST.value());
		apiError.setResponseMessage("Mismatch Type");
		apiError.setErrors(errors);

		return ResponseEntityBuilder.build(apiError);

	}

	// handleConstraintViolationException : triggers when @Validated fails
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolationException(Exception ex, WebRequest request) {

		Map<String, String> errors = new HashMap<>();
		errors.put("message", ex.getMessage());

		ApiError<Map<String, String>> apiError = new ApiError<>();
		apiError.setHttpStatus(HttpStatus.BAD_REQUEST);
		apiError.setResponseCode(HttpStatus.BAD_REQUEST.value());
		apiError.setResponseMessage("Constraint Violation");
		apiError.setErrors(errors);

		return ResponseEntityBuilder.build(apiError);

	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {

		Map<String, String> errors = new HashMap<>();
		errors.put("message", ex.getMessage());

		ApiError<Map<String, String>> apiError = new ApiError<>();
		apiError.setHttpStatus(HttpStatus.BAD_REQUEST);
		apiError.setResponseCode(HttpStatus.BAD_REQUEST.value());
		apiError.setResponseMessage("Error occurred");
		apiError.setErrors(errors);

		return ResponseEntityBuilder.build(apiError);

	}

	@ExceptionHandler({ ApiException.class })
	public ResponseEntity<Object> apiException(ApiException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put("message", ex.getMessage());

		ApiError<Map<String, String>> apiError = new ApiError<>();
		apiError.setHttpStatus(ex.getHttpStatus());
		apiError.setResponseCode(ex.getHttpStatus().value());
		apiError.setResponseMessage(ex.getMessage());
		apiError.setErrors(errors);

		return ResponseEntityBuilder.build(apiError);
	}
}


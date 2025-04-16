package com.samsoft.lms.customer.dto;

import lombok.Data;

@Data
public class CustomerExceptionResponse {
	private String errorResponseCode;
	private String errorResponseMessage;
}

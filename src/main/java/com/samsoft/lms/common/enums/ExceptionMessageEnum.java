package com.samsoft.lms.common.enums;

public enum ExceptionMessageEnum {
	INPUT_EMPTY("Input Payload Can't be Empty!"),
	INVALID_USERID("User Id can't be empty!"),
	INVALID_OTP("Otp Can't be Empty!"),
	DATA_NOT_FOUND("Data not found...");
	
	private String message;

	public String getMessage() {
		return message;
	}

	ExceptionMessageEnum(String message) {
		this.message = message;
	}
}

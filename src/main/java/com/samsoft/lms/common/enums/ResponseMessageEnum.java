package com.samsoft.lms.common.enums;

public enum ResponseMessageEnum {
	OTP_GENRATED("Otp Successfully Sent."),
	OTP_VERIFIED("Otp Successfully Verified."),
	OTP_WRONG("Wrong otp."),
	OTP_GENERATION_ERR("Otp generation error."),
	OTP_NULL_AT_REDIS("Invalid OTP."),
	DATA_GET_SUCCESS("Data received successfully."),
	DATA_NOT_FOUND("Data not found."),
	SUCCESS("Success...!"),
	INVALID_USER("Invalid login credentials..."),
	INVALID_TOKEN("Login session expired, Please login again...");
	
	private String message;
	
	public String getMessage() {
		return message;
	}
	
	ResponseMessageEnum(String message) {
		this.message = message;
	}
}

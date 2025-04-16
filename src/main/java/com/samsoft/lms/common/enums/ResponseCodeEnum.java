package com.samsoft.lms.common.enums;

public enum ResponseCodeEnum {
	
	SUCCESS(200, "SUCCESS"),
	INVALID_INPUT(400,"INVALID_INPUT"),
	INTERNAL_EXCEPTION(500, "Internal Exception"),
	NOT_FOUND(404, "Not Found"),
	FORBIDDEN(403, "Not Found");
	
	
	private Integer code;
	private String mesage;
	
	
	public Integer getCode() {
		return code;
	}


	public String getMessage() {
		return mesage;
	}


	ResponseCodeEnum(Integer code, String message){
		this.code=code;
		this.mesage=message;
	}
	
}

package com.samsoft.lms.agreement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAddrBoardingDto {

	private String customerId;
	private String addrType;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String landMark;
	private String city;
	private String state;
	private String pinCode;
	private String mobile;
	
}

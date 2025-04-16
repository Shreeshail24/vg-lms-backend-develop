package com.samsoft.lms.agreement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAddressAgrDto {

	//private String customerId;
	private Integer addrSeqNo;
	private String addrType;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String city;
	private String state;
	private String pinCode;
	private String prefferedAddress;
	private String addrStatus;
}

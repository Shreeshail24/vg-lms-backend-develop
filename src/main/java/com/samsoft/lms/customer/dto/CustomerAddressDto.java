package com.samsoft.lms.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAddressDto {

	private String customerId;
	private String addrType;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String city;
	private String state;
	private String pinCode;
	private String prefferedAddress;
	private Integer addrSeqNo;
	
}

package com.samsoft.lms.customer.entities;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@AllArgsConstructor	
public class AgrCustomerResponseDto {

	private String customerId;
//	private String customerName;
	private String mobile;
	private String custCategory;
	private Integer custInternalId;
	private String firstName;
	private String middleName;
	private String lastName;
	private String customerType;
	private LocalDate dtUserDate;
	

	public AgrCustomerResponseDto(String customerId,  String mobile, String custCategory,
			Integer custInternalId, String firstName, String middleName, String lastName, String customerType,
			LocalDate dtUserDate) {
		super();
		this.customerId = customerId;
//		this.customerName = customerName;
		this.mobile = mobile;
		this.custCategory = custCategory;
		this.custInternalId = custInternalId;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.customerType = customerType;
		this.dtUserDate = dtUserDate;
	}
}

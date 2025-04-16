package com.samsoft.lms.mis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionDownloadDto {
	
	private String mastAgrId;

	private String customerId;
	
	private String customerName;
	
	private String mobileNo;
	
	private String emailId;
	
	private Double loanAmount;
	
	private String addressLine1;
	
	private String addressLine2;
	
	private String city;
	
	private String state;
	
	private String country;
	
	private String pincode;
	
	private String nbfc;
	
	private final String alternateMobileNo = "";
	
	private final String priority = "";
	
	private final String assignTo = "";
	
	private final String callingType = "";
	
	private String loanId;

	private String installmentDate;
	
	private Integer installmentNo;
	
	private Integer installmentAmount;
	
	private String prevInstallment;

	private Double intersetRate;
	
	private Integer tenor;
	
	private String bankCode;
	
	private String accountNo;
	
	private final String status = "";
	
	private final String callOn = "";
	
	private final String emiDate = "";
	
	private final String nonContactReason = "";
	
	private final String nonContactFollowupOn = "";
	
	private final String pTPDate = "";
	private String disbDate;
	private String originationApplnNo;
}

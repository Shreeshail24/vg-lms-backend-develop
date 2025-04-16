package com.samsoft.lms.newux.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrawDownUpdateReqDetailsResponseDto {
	private Integer requestId;
	private String mastAgrId;
	private Integer rejectReasonCode;
	private String remarksApproval;
	private String userIdDecision;
	private Character status;
	private String repayFreq;
	private Float interestRate = 0.0f;
	private String dtDisbDate;
	private String dtPrinStart;
	private Double approvedAmount;
	private String disbifscCode;
	private String disbaccountNo;
	private String paymentMode;
	private String utrNo;
	private String customerId;
}

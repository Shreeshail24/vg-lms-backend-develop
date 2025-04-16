package com.samsoft.lms.agreement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollateralBoardingDto {

	//private String mastAgrId;
	private String coltrlType;
	private String insuranceReqYn;
	private String status;
	private String handoverYN;
	private String valuationFreq;
	private String servBranch;
	private Double coltrlValue;
	private String dtValuation;
	private String dtCreation;
//	private String releaseNote;
//	private String releaseReason;
}

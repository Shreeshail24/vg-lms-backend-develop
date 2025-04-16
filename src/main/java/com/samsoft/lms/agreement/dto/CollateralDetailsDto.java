package com.samsoft.lms.agreement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CollateralDetailsDto {
	private Integer coltrlId;	
	private String coltrlType;
	private String insuranceReqYn;
	private String dtValuation;
	private String dtCreation;
	private String status;
	private String handoverYN;
	private String valuationFreq;
	private String servBranch;
	private Double coltrlValue;
	private String releaseNote;
	private String releaseReason;
}

package com.samsoft.lms.agreement.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PdcBoardingDto {

//	private String mastAgrId;
	private Integer srNo;
	private String dtInstrumentDate;
	private String bankCode;
	private String bankBranchCode;
	private String accountNo;
	private String accountType;
	private String instrumentNo;	
	private Double intrumentAmount;
	private String ifscCode;
	private String micrCode;	
	private String clearingLocation;	
	private String instrumentStatus;
	private String depositBank;
	private String dtReceipt;
	
}

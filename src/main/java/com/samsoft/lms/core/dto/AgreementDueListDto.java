package com.samsoft.lms.core.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgreementDueListDto {

	private Integer dueDtlId;
	private String mastAgrId;
	private String loanId;
	private String dtDueDate;
	private String dueCategory;
	private String dueHead;
	private Double dueAmount;
	private Integer installmentNo;
	private Integer tranDtlId;
	private String customerName;
	private String gender;
	private String losApplicationNo;
	private String mobile;
	private Double loanAmount;
	private String umrn;
	private String colender;
	private Double interestAmount;
	private Double principalAmount;
	private Float interestRate;
	private String disbDate;
}

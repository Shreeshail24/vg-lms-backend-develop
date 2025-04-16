package com.samsoft.lms.core.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VAgrInterestAccrualHistoryDto {
	
	private String mastAgrId;
	private String loanId;
	private String dtTranDate;
	private Integer tranId;
	private String tranType;
	private String remark;
	private Double debitAmount;
	private Double creditAmount;
}

package com.samsoft.lms.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecallReqLoanReceivableListDto {
	public String loanId;
	public String portfolio;
	public double outstandingAmount;
	public double bpiAmount;
	public double totalOutstanding;

}
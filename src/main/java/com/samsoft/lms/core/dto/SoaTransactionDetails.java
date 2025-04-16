package com.samsoft.lms.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SoaTransactionDetails {

	private String dtTran;
	private String description;
	private String referenceId;
	private Double debitAmount;
	private Double creditAmount;
	private String transactionType;
}

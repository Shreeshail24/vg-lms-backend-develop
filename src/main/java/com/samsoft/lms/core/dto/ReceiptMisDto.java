package com.samsoft.lms.core.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptMisDto {

	private Integer colenderId;
	private String customerId;
	private String loanId;
	private String customerName;
	private String product;
	private String dtDisb;
	private Double loanAmount;
	private String emiDueDate;
	private Double EmiDue;
	private Double principalAmountDue;
	private Double interestAmountDue;
	private Double AmountPaid;
	private Double principalAmountPaid;
	private Double interestAmountPaid;
	private Double otherCharges;
	private String paymentDate;
	private String paymentStatus;
}

package com.samsoft.lms.transaction.dto;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChargeBookedHistDto {

	private Integer tranId;
	private Double waivableAmount;
	private Integer tranDtlId;
	private Integer dueDtlId;
	private String mastAgrId;
	private String tranType;
	private String tranHead;
	private String dtTranDate;
	private String loanId;
	private String reason;
	private String remark;
	private Double waiverAmount;
	private Integer installmentNo=0;
}

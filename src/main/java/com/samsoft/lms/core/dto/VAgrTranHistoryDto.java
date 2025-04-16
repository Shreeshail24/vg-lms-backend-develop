package com.samsoft.lms.core.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VAgrTranHistoryDto {
	private Integer SrNo;
	private Integer tranId;
	private String dtTranDate;
	private String tranType;
	private String remark;
	private Double debitAmount;
	private Double creditAmount;
	private String tranCategory;
	private String userId;
	private String formatDate;

}

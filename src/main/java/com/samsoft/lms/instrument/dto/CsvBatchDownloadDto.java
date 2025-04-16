package com.samsoft.lms.instrument.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CsvBatchDownloadDto {

	private String mastAgrId;
	private Date instrumentDate;
	private Integer instrumentNo;
	private String ifscCode;
	private String accountNo;
	private String accountType;
	private Double instrumentAmount;
	private String instrumentStatus;
	private String bounceReason;
}

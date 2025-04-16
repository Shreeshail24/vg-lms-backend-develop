package com.samsoft.lms.instrument.dto;

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
public class BatchListDto {
	
	private Integer batchId;
	@Temporal(TemporalType.DATE)
	private String dtBatchDate;
	private String instrumentType;
	private String depositBankIfsc;
	private String depositBankName;
	private String depositBankBranch;
	private Integer totalInstruments;
	private Integer totalClearedInstruments;
	private Integer totalBounceInstruments;
	private String batchStatus;
	private String userId;

}

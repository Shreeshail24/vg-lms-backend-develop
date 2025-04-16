package com.samsoft.lms.instrument.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchDetailsListDto {
	
	private String mastAgrId;
	private String dtInstrumentDate;
	private String instrumentStatus;
	private String bounceReason;
	private Double instrumentAmount;
	private String instrumentType;
	private Integer instrumentId;
	private String umrn;

}

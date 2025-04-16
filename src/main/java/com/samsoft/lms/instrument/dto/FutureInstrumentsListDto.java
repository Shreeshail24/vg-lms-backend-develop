package com.samsoft.lms.instrument.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FutureInstrumentsListDto {
	private String accountNo;
	private String instrumentNo;
	@Temporal(TemporalType.DATE)
	private String dtInstrumentDate;
	private String instrumentType;
	private String umrn;
	private Double instrumentAmount;
	

}

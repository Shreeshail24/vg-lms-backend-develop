package com.samsoft.lms.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VAgrTranHistoryOdHeaderDto {
	
	private String customer;
	private String mastAgrId;
	private String interestAccrual;
	private Double sanctionedLimit; 
	private Double utilizedLimit;
	private Double availableLimit; 
	private Float interestRate;
	private Integer tenor;
	private String dropLineFreq;
	private Double drawingPower;

}

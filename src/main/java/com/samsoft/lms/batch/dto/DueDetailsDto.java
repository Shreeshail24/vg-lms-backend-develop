package com.samsoft.lms.batch.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DueDetailsDto {

	 private Date dtDueDate;
	 private Integer installmentNo;
	 private Double installmentDue;
	
	
}
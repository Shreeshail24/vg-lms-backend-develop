package com.samsoft.lms.agreement.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ColenderBoardingDto {	

	//private String mastAgrId;
	private String colenderCode;
	private Integer principalShare;
	private Integer interestShare;
	private String colenderApplNo;
	private String colenderAgrNo;
	private String colenderCustId;
	private List<ColenderIncShareBoardingDto> colenderShare;
	private String instrumentPresenterYn;


}


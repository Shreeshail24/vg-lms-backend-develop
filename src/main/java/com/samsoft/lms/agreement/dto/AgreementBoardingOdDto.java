package com.samsoft.lms.agreement.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgreementBoardingOdDto {
	
	private MasterAgreementBoradingDto masterAgreement; 
	private List<CustomerBoardingDto> customerList;
	private CustomerLimitBoardingDto customerLimit; 
	private ProductBoardingOdDto product;	
	private List<ColenderBoardingDto> colender; //NM
//	private PdcBoardingDto pdc; //NM
	private EpayBoardingDto epay; //NM
	private List<FeeBoardingDto> feeList; 

}

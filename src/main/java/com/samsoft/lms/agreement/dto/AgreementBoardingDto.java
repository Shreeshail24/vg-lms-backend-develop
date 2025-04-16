package com.samsoft.lms.agreement.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgreementBoardingDto {
	
	private MasterAgreementBoradingDto masterAgreement; 
	private List<CustomerBoardingDto> customerList;
	private ProductBoardingDto product;	
	private List<ColenderBoardingDto> colender; //NM
//	private PdcBoardingDto pdc; //NM
	private EpayBoardingDto epay; //NM
	private List<CollateralBoardingDto> collateral; //NM
	private List<FeeBoardingDto> feeList;
	private InsuranceDetailsBoardingDto insuranceDetails;

}

package com.samsoft.lms.request.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DreDtoManual implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1813856886769706334L;
	
	private String manualLoanId;
	private String manualDueDate;
	private String manualTranCategory;
	private String manualTranHead;
	private Double manualAmount =0.0d;
	private Integer manualInstallmentNo;
	private Integer manualChargeBookTranId;
	
}

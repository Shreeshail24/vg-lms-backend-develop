package com.samsoft.lms.request.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DreAllocationDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3486693861855320856L;
	
	private String loanId;
	private Double allocatedAmount =0.0;
	private String tranCategory;
	private String tranHead;
	private Integer installmentNo;
	private Integer chargeBookTranId;

}

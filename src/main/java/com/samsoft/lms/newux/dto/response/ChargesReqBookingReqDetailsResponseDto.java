package com.samsoft.lms.newux.dto.response;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.samsoft.lms.request.dto.DreAllocationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargesReqBookingReqDetailsResponseDto {

	private String masterAgreementId;
	private String requestDate;	
	private String flowType;
	private String requestStatus;
	private String userId;
	private String loanId;
	private String tranHead;
	private double chargeAmount;
	private String chargeBookReason;
	private String chargeBookRemark;
	private int installmentNo;
	private String customerId;
	private String allocatedUserId;
	private String customerName;
	
	private double totalAmount;
	
}

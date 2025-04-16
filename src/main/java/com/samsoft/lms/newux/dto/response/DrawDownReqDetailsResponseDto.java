package com.samsoft.lms.newux.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrawDownReqDetailsResponseDto {

	private String masterAggrId;
	private Double limitSanAmount;
	private Double utilizedLimit;
	private Double availableLimit;
	private Double totalDues;
	private Double totalOverDues;
	private Double requestedAmount;
	private Double approvedAmount;
	private String remarksRequest;
	private String userIdRequest;
	private String endUse;
	private String requestDate;
	private String userId;
	private String customerId;
}

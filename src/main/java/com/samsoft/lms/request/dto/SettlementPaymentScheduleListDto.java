package com.samsoft.lms.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SettlementPaymentScheduleListDto {

	private String dtPromise;
	private double promiseAmount;
}

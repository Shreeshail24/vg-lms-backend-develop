package com.samsoft.lms.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VAgrTranHistoryHeaderDto {

	private Double principalOutstanding;
	private Double dueBalance;
	private Double installmentDue;
	private Double chargesDue;
	private Double excessAmount;
}

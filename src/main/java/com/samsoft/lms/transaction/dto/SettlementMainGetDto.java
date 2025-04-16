package com.samsoft.lms.transaction.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SettlementMainGetDto {

	private int totalRows;
	private List<SettlementGetDto> settlementList;
}

package com.samsoft.lms.transaction.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ForclosureReceivableListDto {

	private Double totalReceivables;
	private Double netReceivables;
	private Double excess;
	private List<ForclosureDueDetails> dueList;
}

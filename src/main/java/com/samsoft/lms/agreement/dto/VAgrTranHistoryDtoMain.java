package com.samsoft.lms.agreement.dto;

import java.util.List;

import com.samsoft.lms.core.dto.VAgrTranHistoryDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VAgrTranHistoryDtoMain {
	private Integer totalRows;
	private List<VAgrTranHistoryDto> historyList;
}

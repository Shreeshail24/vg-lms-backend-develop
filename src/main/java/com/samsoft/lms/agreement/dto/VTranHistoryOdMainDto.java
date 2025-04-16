package com.samsoft.lms.agreement.dto;

import java.util.List;

import com.samsoft.lms.core.entities.VAgrTranHistoryOd;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VTranHistoryOdMainDto {

	private Integer totalRows;
	List<VAgrTranHistoryOd> tranList;
}

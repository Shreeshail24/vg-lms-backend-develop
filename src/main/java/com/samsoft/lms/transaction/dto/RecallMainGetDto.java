package com.samsoft.lms.transaction.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecallMainGetDto {

	private int totalRows;
	private List<RecallGetDto> recallList = new ArrayList<>();
}

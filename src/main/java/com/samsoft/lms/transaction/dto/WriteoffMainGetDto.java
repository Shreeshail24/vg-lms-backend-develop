package com.samsoft.lms.transaction.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WriteoffMainGetDto {

	private int totalRows;
	private List<WriteoffGetDto> writeoffList;
	
}

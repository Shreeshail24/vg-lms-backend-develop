package com.samsoft.lms.batch.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EodStatusMainDto {
	Integer totalRows;
	List<EodStatusDto> eodStatus;

}

package com.samsoft.lms.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AllDrawdownRequestIdMainDto {

	private Integer totalRows;
	private Integer allCount;
	private Integer pendAppCount;
	private Integer rejCount;
	private Integer pndDisbCount;
	private Integer disbCount;
	List<AllDrawdownRequestIdDto> allRequestDto;
}

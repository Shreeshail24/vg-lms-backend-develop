package com.samsoft.lms.newux.dto.response.getloans;

import java.util.List;

import com.samsoft.lms.newux.dto.response.SrWorklistByActivityCodeAndStatusResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllSRWithActCodeAndStatusResDto {
	    private Integer allSRCount;
	    private Integer pendingSRCount;
	    private Integer approvedSRCount;
	    private Integer rejectedSRCount;
	    private List<SrWorklistByActivityCodeAndStatusResponseDto> serviceRequests;
	    private String type;
}

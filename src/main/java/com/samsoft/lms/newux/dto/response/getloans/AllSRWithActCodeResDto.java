package com.samsoft.lms.newux.dto.response.getloans;

import java.util.List;

import com.samsoft.lms.newux.dto.response.SrWorklistByActivityCodeResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllSRWithActCodeResDto {
	    private Integer allSRCount;
	    private Integer pendingSRCount;
	    private Integer approvedSRCount;
	    private Integer rejectedSRCount;
	    private List<SrWorklistByActivityCodeResponseDto> serviceRequests;
	    private String type;
}

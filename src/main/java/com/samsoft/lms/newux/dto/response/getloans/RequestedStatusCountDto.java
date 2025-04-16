package com.samsoft.lms.newux.dto.response.getloans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; 

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RequestedStatusCountDto {
	
	  private Integer allSRCount;
	    private Integer pendingSRCount;
	    private Integer approvedSRCount;
	    private Integer rejectedSRCount;
	    private Integer verifiedSRCount;
	    private String type;

}


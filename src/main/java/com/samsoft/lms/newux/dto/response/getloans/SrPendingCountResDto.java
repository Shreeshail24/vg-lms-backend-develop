package com.samsoft.lms.newux.dto.response.getloans;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SrPendingCountResDto {
	    private Integer debitSrPendingCount;
	    private Integer creditSrPendingCount;
	    private Integer recepitSrPendingCount;
	    private Integer refundSrPendingCount;
	    private Integer forclousreSrPendingCount;
	    private Integer overdraftSrPendingCount;
	    private Integer chargesBookingSrPendingCount;
	    private Integer chargesWaverSrPendingCount;
}

package com.samsoft.lms.las.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetLasPortfolioSummaryResDto {

    private Integer totalNoOfLoans;
    private Double totalSanctionLimit;
    private Double totalUtilizedLimit;
    private Double totalValuation;
    private Double portfolioLtv;
    private List<LasPortfolioDetailsResDto> lasPortfolioDetails;

}

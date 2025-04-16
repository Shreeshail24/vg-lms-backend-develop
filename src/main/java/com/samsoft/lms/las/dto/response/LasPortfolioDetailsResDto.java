package com.samsoft.lms.las.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LasPortfolioDetailsResDto {

    private String masterAgrId;
    private String customerId;
    private Double sanctionLimit;
    private Double fmv;
    private Double utilizedLimit;
    private Double actualLtv;
    private String customerName;
    private String homeBranch;
}

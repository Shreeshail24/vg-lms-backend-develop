package com.samsoft.lms.agreement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsuranceDetailsBoardingDto {

    private String insuranceCompany;
    private Double insurancePremiumAmount;
}

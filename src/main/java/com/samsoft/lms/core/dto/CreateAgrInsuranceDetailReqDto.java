package com.samsoft.lms.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAgrInsuranceDetailReqDto {

    @NotNull(message = "The master agreement id is required.")
    private String mastAgrId;

    @NotNull(message = "The insurance company is required.")
    private String insuranceCompany;

    @NotNull(message = "The insurance premium amount is required.")
    private Double insurancePremiumAmount;

}

package com.samsoft.lms.banking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBankPaymentVAVerifyReqDto {

    private String virtualId;

    private Double amount;

    private String vaValidationJsonData;


}

package com.samsoft.lms.banking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBankPaymentReqDto {

    private Double amount;
    private String utrNo;
    private String instaAlertJsonData;
    private String paymentStatus;
}

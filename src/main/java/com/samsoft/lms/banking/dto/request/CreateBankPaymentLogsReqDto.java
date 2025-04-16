package com.samsoft.lms.banking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBankPaymentLogsReqDto {

    private Integer paymentId;
    private Integer vaPaymentId;
    private String jsonData;
    private String error;
}

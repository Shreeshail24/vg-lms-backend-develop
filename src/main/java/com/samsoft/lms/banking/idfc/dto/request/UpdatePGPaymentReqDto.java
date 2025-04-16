package com.samsoft.lms.banking.idfc.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePGPaymentReqDto {

    private String paymentSessionId;
    private String orderId;
    private String status;
}

package com.samsoft.lms.banking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBankPaymentReqDto {

    @NotNull(message = "The Virtual Id is Required.")
    private String virtualId;

    @NotNull(message = "The Amount is Required.")
    private Double amount;

    private String vaValidationJsonData;

    @NotNull(message = "The Source is Required.")
    private String source;

    @NotNull(message = "The Payment Status is Required.")
    private String paymentStatus;

    //Payment Gateway
    private String paymentSessionId;
    private String cfOrderId;
    private String orderStatus;
    private String paymentGatewayJsonData;
    private String mastAgrId;
    private String customerName;
}

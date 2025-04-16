package com.samsoft.lms.banking.idfc.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CallbackInstaAlertReqDto {

    @JsonProperty("customerCode")
    private String customerCode;

    @JsonProperty("customerName")
    private String customerName;

    @JsonProperty("productCode")
    private String productCode;

    @JsonProperty("productDescription")
    private String productDescription;

    @JsonProperty("poolingAccountNumber")
    private String poolingAccountNumber;

    @JsonProperty("transactionType")
    private String transactionType;

    @JsonProperty("dataKey")
    private String dataKey;

    @JsonProperty("batchAmt")
    private String batchAmt;

    @JsonProperty("batchCrAmtCcd")
    private String batchCrAmtCcd;

    @JsonProperty("creditDate")
    private String creditDate;

    @JsonProperty("vaNumber")
    private String vaNumber;

    @JsonProperty("utrNo")
    private String utrNo;

    @JsonProperty("creditGenerationTime")
    private String creditGenerationTime;

    @JsonProperty("remitterName")
    private String remitterName;

    @JsonProperty("remitterAccountNumber")
    private String remitterAccountNumber;

    @JsonProperty("remittingBankName")
    private String remittingBankName;

    @JsonProperty("ifscCode")
    private String ifscCode;

}

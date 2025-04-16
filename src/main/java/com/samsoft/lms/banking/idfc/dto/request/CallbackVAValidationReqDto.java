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
public class CallbackVAValidationReqDto {

    @JsonProperty("VANum")
    private String vANum;

    @JsonProperty("remitterAc")
    private String remitterAc;

    @JsonProperty("remiterName")
    private String remiterName;

    @JsonProperty("remitterAcType")
    private String remitterAcType;

    @JsonProperty("remitterBankifsc")
    private String remitterBankifsc;

    @JsonProperty("txnAmt")
    private String txnAmt;

    @JsonProperty("remitterBranch")
    private String remitterBranch;

    @JsonProperty("bankRef")
    private String bankRef;

    @JsonProperty("remitterBank")
    private String remitterBank;

}

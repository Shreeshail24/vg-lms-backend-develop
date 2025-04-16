package com.samsoft.lms.banking.idfc.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CallbackInstaAlertResDto {

    @JsonProperty("corRefNo")
    private String corRefNo;

    @JsonProperty("ReqrefNo")
    private String reqrefNo;

    @JsonProperty("status")
    private String status;

    @JsonProperty("statusDesc")
    private String statusDesc;

    @JsonProperty("errorCode")
    private String errorCode;

    @JsonProperty("errorDesc")
    private String errorDesc;

}

package com.samsoft.lms.newux.dto.response;

import com.samsoft.lms.agreement.entities.AgrInvoiceDetails;
import com.samsoft.lms.aws.dto.DocumentViewResDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAgrInvoiceDetailsResDto {

    private AgrInvoiceDetails invoiceDetails;
    private DocumentViewResDto invoiceDocumentView;
}

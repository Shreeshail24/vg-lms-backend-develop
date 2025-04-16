package com.samsoft.lms.agreement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AgrInvoiceDetailsDto {

	private String invoiceNo;
	private String dtInvoiceDate;
	private String supplierName;
	private double invoiceSubTotalAmount;
	private double invoiceTaxAmount;
	private double netInvoiceAmount;
	private int creditDays;
	private String invoiceImageURL;

}

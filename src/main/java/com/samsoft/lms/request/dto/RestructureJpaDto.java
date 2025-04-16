package com.samsoft.lms.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestructureJpaDto {
	
	public RestructureJpaDto(String mastAgrId, String dueCategory, String dueHead, Double dueAmount, 
			Double dueTaxAmount) {
		super();
		this.dueCategory = dueCategory;
		this.dueHead = dueHead;
		this.dueAmount = dueAmount;
		this.mastAgrId = mastAgrId;
		this.dueTaxAmount = dueTaxAmount;
	}
	private String dueCategory;
	private String dueHead;
	private Double dueAmount;
	private String mastAgrId;
	private Double dueTaxAmount;
	
	

}
